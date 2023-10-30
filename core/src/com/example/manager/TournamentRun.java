package com.example.manager;

import com.example.manager.player.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TournamentRun extends Run {

    AtomicInteger completed = new AtomicInteger(0);

    private class BracketNode {

        private final Object handlerLock = new Object();
        private final Object schedulingLock = new Object();

        private final List<CompletionHandler<BracketNode>> handlers = new ArrayList<>();


        Game game1;
        Game game2;
        Game game3;

        protected int p1 = -1;
        protected int p2 = -1;

        int winner = 0;
        private final GameConfig config;

        private int completed = 0;

        private BracketNode(GameConfig config) {
            this.config = config;
        }

        public void makeLeaf(int p1, int p2) {
            this.p1 = p1;
            this.p2 = p2;
            startGames();
        }

        BracketNode left;
        BracketNode right;

        public void setLeft(BracketNode left) {
            this.left = left;
            left.addCompletionListener(this::onLeftCompletion);
        }

        public void setLeft(int p1) {
            synchronized (schedulingLock) {
                this.p1 = p1;
                if (p2 > -1) startGames();
            }
        }

        protected void onLeftCompletion(BracketNode left) {
            setLeft(left.getWinner());
        }

        public void setRight(BracketNode right) {
            this.right = right;
            right.addCompletionListener(this::onRightCompletion);
        }

        public void setRight(int p2) {
            synchronized (schedulingLock) {
                this.p2 = p2;
                if (p1 > -1) startGames();
            }
        }

        protected void onRightCompletion(BracketNode right) {
            setRight(right.getWinner());
        }

        public int getWinner() {
            return winner > 0 ? p1 : p2;
        }

        public int getLooser() {
            return winner > 0 ? p2 : p1;
        }

        private void startGames() {
            config.players = new ArrayList<>();
            config.players.add(players.get(p1));
            config.players.add(players.get(p2));
            config.mapName = "lukeMap"; //ToDo make dynamic
            game1 = new Game(config);
            game1.addCompletionListener(this::onGameComplete);
            config.mapName = "Gadsrena"; //ToDo make dynamic
            game2 = new Game(config);
            game2.addCompletionListener(this::onGameComplete);
            config.mapName = "mondlandschaft"; //ToDo make dynamic
            game3 = new Game(config);
            game3.addCompletionListener(this::onGameComplete);
            manager.schedule(game1);
            manager.schedule(game2);
            manager.schedule(game3);
        }

        void onGameComplete(Executable exec) {
            Game game = (Game) exec;
            System.out.println("Completed: " + TournamentRun.this.completed.incrementAndGet());
            synchronized (handlerLock) {
                completed++;
                winner = (int) (game.getScores()[0] - game.getScores()[1]);
                if (completed >= 3) {
                    System.out.printf("|%s-%s|%n",players.get(p1).getName(),players.get(p2).getName());
                    if (winner == 0)
                        System.err.printf("Warning no Winner in Best of 3 %s vs %s", config.players.get(0).getName(), config.players.get(1).getName());
                    for (CompletionHandler<BracketNode> handler : handlers) {
                        handler.onComplete(this);
                    }
                }
            }
        }

        void addCompletionListener(CompletionHandler<BracketNode> handler) {
            synchronized (handlerLock) {
                handlers.add(handler);
                if (completed >= 3) handler.onComplete(this);
            }
        }

    }

    private class LooserBracket extends BracketNode{
        private LooserBracket(GameConfig config) {
            super(config);
        }

        @Override
        protected void onLeftCompletion(BracketNode left) {
            setLeft(left.getLooser());
        }

        @Override
        protected void onRightCompletion(BracketNode right) {
            setRight(right.getLooser());
        }
    }



    private final ArrayList<Class<? extends Player>> players;

    int completedGames = 0;

    private BracketNode finalGame;
    private BracketNode redemptionFinal;

    private BracketNode winnerFinal;
    private BracketNode looserFinal;

    private final float[] scores;

    protected TournamentRun(Manager manager, RunConfiguration runConfig) {
        super(manager, runConfig);
        if (runConfig.teamCount != 2)
            System.err.printf("Warning: Only 1v1 is supported in bracket tournaments. Ignoring config.teamCount = %d%n", runConfig.teamCount);
        runConfig.teamCount = 2;
        players = runConfig.players;

        int playerCount = players.size();
        scores = new float[playerCount];
        if (playerCount < 4) {
            System.err.println("A Tournament requires at least 4 players");
            return;
        }

        if ((playerCount & playerCount - 1) != 0) {
            System.err.printf("Tournament only supports a power of 2 for Number of players(=%d).", playerCount);
            return;
        }

        finalGame = new BracketNode(new GameConfig(runConfig));
        finalGame.addCompletionListener(this::onRootCompletion);

        List<BracketNode> winnerLeafGames = new ArrayList<>();
        List<BracketNode> looserHeadGames = new ArrayList<>();

        //
        int capacity = 2;

        winnerFinal = new BracketNode(new GameConfig(runConfig));

        winnerLeafGames.add(winnerFinal);


        while (capacity < playerCount) {
            ArrayList<BracketNode> newWinnerLeafGames = new ArrayList<>();
            List<BracketNode> looserLeafGames = new ArrayList<>();
            for (BracketNode cur: winnerLeafGames
                 ) {

                BracketNode b1 = new BracketNode(new GameConfig(runConfig));
                BracketNode b2 = new BracketNode(new GameConfig(runConfig));

                cur.setLeft(b1);
                cur.setRight(b2);

                newWinnerLeafGames.add(b1);
                newWinnerLeafGames.add(b2);

                LooserBracket looserLeaf = new LooserBracket(new GameConfig(runConfig));
                looserLeafGames.add(looserLeaf);
                looserLeaf.setLeft(b1);
                looserLeaf.setRight(b2);
            }

            looserHeadGames.add(makeTurnament(looserLeafGames, runConfig));
            winnerLeafGames = newWinnerLeafGames;
            BracketNode newNode = new BracketNode(new GameConfig(runConfig));
            capacity*=2;
        }
        looserFinal = new BracketNode(new GameConfig(runConfig));
        winnerFinal.addCompletionListener(bracket -> looserFinal.setRight(bracket.getLooser()));
        BracketNode curTail = looserFinal;
        BracketNode next = null;

        assert looserHeadGames.size()>0;

        for (BracketNode cur: looserHeadGames) {
            if (next != null){
                BracketNode nextTail = new BracketNode(new GameConfig(runConfig));
                nextTail.setRight(next);
                curTail.setLeft(nextTail);
                curTail = nextTail;

            }
            next = cur;
        }
        curTail.setLeft(next);

        int pIndex = 0;

        for (BracketNode cur: winnerLeafGames
             ) {
            cur.makeLeaf(pIndex++, pIndex++);
        }


        finalGame = new BracketNode(new GameConfig(runConfig));
        finalGame.setLeft(winnerFinal);
        finalGame.setRight(looserFinal);
        finalGame.addCompletionListener(this::onRootCompletion);

        redemptionFinal = new LooserBracket(new GameConfig(runConfig));
        redemptionFinal.setLeft(winnerFinal);
        redemptionFinal.setRight(looserFinal);
        redemptionFinal.addCompletionListener(this::onRootCompletion);

        assert pIndex == playerCount;

    }

    private BracketNode makeTurnament(List<BracketNode> leafs, RunConfiguration runConfig){
        assert  ((leafs.size() & leafs.size() - 1) == 0);
        List<BracketNode> newNodes;
        while (leafs.size() > 1){
            newNodes = new ArrayList<>();
            BracketNode last = null;
            for (BracketNode cur: leafs
                 ) {
                if (last == null) last = cur;
                else {
                    BracketNode next = new BracketNode(new GameConfig(runConfig));
                    newNodes.add(cur);
                    next.setLeft(last);
                    next.setRight(cur);
                    last = null;
                }
            }
            leafs = newNodes;
        }
        return leafs.get(0);
    }

    public synchronized void onRootCompletion(BracketNode node) {
        completedGames++;
        if (completedGames == 2) complete();
    }

    @Override
    protected void complete() {

        int winner = finalGame.getWinner();
        int second = finalGame.getLooser();
        scores[redemptionFinal.getWinner()] = 3f;
        if(scores[redemptionFinal.getLooser()] == 0f) scores[redemptionFinal.getLooser()] = 4f;
        setLooserScore(finalGame.left.left, 5f);
        setLooserScore(finalGame.left.right, 5f);
        setLooserScore(finalGame.right.left, 5f);
        setLooserScore(finalGame.right.right, 5f);
        scores[winner] = 1f;
        scores[second] = 2f;

        ArrayList<BracketNode> curLayer = new ArrayList<>();
        ArrayList<BracketNode> nextLayer;
        curLayer.add(winnerFinal);

        System.out.println("Winner:" + players.get(winner).getName());
        System.out.printf("Final: " + printBracket(finalGame));
        System.out.println("3rd:" + players.get(redemptionFinal.getWinner()).getName());
        System.out.printf("Redemption: " + printBracket(redemptionFinal));
        System.out.println("MainBracket:");
        while (!curLayer.isEmpty()){
            nextLayer = new ArrayList<>();
            for (BracketNode cur : curLayer
            ) {
                System.out.print(printBracket(cur));
                if (cur.left != null) nextLayer.add(cur.left);
                if (cur.right != null) nextLayer.add(cur.right);
            }
            curLayer = nextLayer;
            System.out.println("");
        }
        curLayer.add(looserFinal);
        System.out.println("LooserBracket:");
        while (!curLayer.isEmpty()){
            nextLayer = new ArrayList<>();
            for (BracketNode cur : curLayer
            ) {
                System.out.print(printBracket(cur));
                if (cur.left != null) nextLayer.add(cur.left);
                if (cur.right != null) nextLayer.add(cur.right);
            }
            curLayer = nextLayer;
            System.out.println("");
        }

        super.complete();
    }

    private String printBracket(BracketNode node){
        int s1 = (node.winner + 3)/2;
        int s2 = 3-s1;
        return String.format("|%s-%s:%d-%d|", players.get(node.p1).getName(),players.get(node.p2).getName(), s1, s2);
    }

    private void setLooserScore(BracketNode node, float score) {
        if (node == null) return;
        scores[node.getLooser()] = score;
        setLooserScore(node.left, score + 1);
        setLooserScore(node.right, score + 1);

    }

    @Override
    public float[] getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "TournamentRun{" +
                "super=" + super.toString() +
                ", players=" + players +
                ", completedGames=" + completedGames +
                ", finalGame=" + finalGame +
                ", looserBracket=" + redemptionFinal +
                ", scores=" + Arrays.toString(scores) +
                '}';
    }
}

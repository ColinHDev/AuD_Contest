package bots;

import com.gatdsen.manager.Controller;
import com.gatdsen.manager.StaticGameState;
import com.gatdsen.manager.player.Bot;
import com.gatdsen.simulation.Tower;

/**
 * In dieser Klasse implementiert ihr euren Bot.
 */
public class TestBot extends Bot {

    /**
     * Hier müsst ihr euren vollständigen Namen angeben
     * @return Euer vollständiger Name im Format: "Vorname(n) Nachname"
     */
    @Override
    public String getStudentName() {
        return "Max Musterstudent 2";
    }

    /**
     * Hier müsst ihr eure Matrikelnummer angeben
     * @return Eure Matrikelnummer
     */
    @Override
    public int getMatrikel() {
        return 1338;
    }

    /**
     * Hier könnt ihr eurem Bot einen (kreativen) Namen geben
     * @return Der Name eures Bots
     */
    @Override
    public String getName() {
        return "Beathe Beispiel-Bot 2";
    }

    /**
     * Wird vor Beginn des Spiels aufgerufen. Die erlaubte Berechnungszeit für diese Methode beträgt 1 Sekunde.
     * Diese Methode kann daher verwendet werden, um Variablen zu initialisieren und einmalig, sehr rechenaufwändige
     * Operationen durchzuführen.
     * @param state Der {@link StaticGameState Spielzustand} zu Beginn des Spiels
     */
    @Override
    public void init(StaticGameState state) {
        System.out.println("Der Bot \"" + getName() + "\" wurde initialisiert!");
    }

    /**
     * Wird aufgerufen, wenn der Spieler seinen Zug für die aktuelle Runde durchführen soll. Die erlaubte
     * Berechnungszeit für diese Methode beträgt 0,5 Sekunden bzw. 500 Millisekunden.
     * <p>
     * Der {@link StaticGameState Spielzustand} reflektiert dabei den Zustand des Spiels vor dem Zug des Spielers. Der
     * Zustand ist statisch, das heißt bei Aufrufen des {@link Controller Controllers} werden diese Änderungen nicht im
     * Spielzustand in dieser Runde reflektiert, sondern erst in der nächsten Runde, wenn man den neuen Spielzustand erhält.
     * <p>
     * Der Controller ermöglicht dir die Steuerung, um Aktionen, wie bspw. das Platzieren von Türmen, auszuführen. Die
     * übergebene Controller-Instanz deaktiviert sich nach Ende des Zuges permanent.
     * @param state Der {@link StaticGameState Spielzustand} vor der Ausführung des aktuellen Zuges
     * @param controller Der {@link Controller Controller}, um Aktionen auszuführen
     */
    @Override
    public void executeTurn(StaticGameState state, Controller controller) {
        System.out.println("Der Bot \"" + getName() + "\" ist am Zug in Runde " + state.getTurn() + "!");
        controller.placeTower(
                8, 16,
                Tower.TowerType.BASIC_TOWER
        );
    }
}
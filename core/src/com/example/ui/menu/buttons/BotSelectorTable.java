package com.example.ui.menu.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.example.manager.Manager;
import com.example.manager.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class BotSelectorTable extends LimitedWidthTable {

	ArrayList<SelectBox<Manager.NamedPlayerClass>> botSelectors;

	Manager.NamedPlayerClass[] availableBots;

	int currentBotAmount;

	public BotSelectorTable(Skin skin, int columnWidth) {
		super(skin, columnWidth);
		botSelectors = new ArrayList<>();
		currentBotAmount = 0;
	}

	/**
	 * Adjusts the number of Bot SelectBoxes displayed on screen.
	 * Will rebuild the table so it is not really efficient, creating a new data structure for it
	 * would be a time investment.(layoutPackage was meant for it, but i did not want to focus on that)
	 */
	public void resizeTable(int amountOfBots) {

		//make sure botSelectors has enough select boxes
		fillBotSelectorList(amountOfBots - botSelectors.size());


		this.resetTable();


		Iterator<SelectBox<Manager.NamedPlayerClass>> currentBotSelection = botSelectors.iterator();

		for (int i = 0; i < amountOfBots; i++) {
			this.add(currentBotSelection.next());
		}

		currentBotAmount = amountOfBots;
	}


	/**
	 * creates new Select Boxes and puts them into {@link BotSelectorTable#botSelectors}
	 *
	 * @param neededSelectBoxes Number of Select boxes to add to the list
	 */
	private void fillBotSelectorList(int neededSelectBoxes) {
		//botSelectors is not shrinked, because if we increase
		// the number of selections again, we remember previously selected values

		for (int i = 0; i < neededSelectBoxes; i++) {
			SelectBox<Manager.NamedPlayerClass> newBox = new SelectBox<>(this.getSkin());
			newBox.setItems(availableBots);
			botSelectors.add(newBox);
		}
	}

	/**
	 * Gets the selected Bots from {@link BotSelectorTable#botSelectors} and returns them inside a List.
	 *
	 * @return
	 */
	public ArrayList<Class<? extends Player>> evaluateSelected() {
		ArrayList<Class<? extends Player>> selectedBots = new ArrayList<>();
		Iterator<SelectBox<Manager.NamedPlayerClass>> botSelectorsIterator = botSelectors.iterator();
		//only add the number of bots corresponding to the currently selected ones
		for (int i = 0; i < currentBotAmount; i++) {
			selectedBots.add(botSelectorsIterator.next().getSelected().getClassRef());
		}

		return selectedBots;
	}

	/**
	 * Sets the List of bots that can be selected from the SelectBoxes
	 *
	 * @param bots
	 */
	public void setAvailableBots(Manager.NamedPlayerClass[] bots) {
		this.availableBots = bots;
		for (SelectBox<Manager.NamedPlayerClass> box : botSelectors) {
			box.setItems(availableBots);
		}
	}

	/**
	 * Tries to set the passed List as the selected values, if a value can not bo set, the default value stays set.
	 *
	 * @param players
	 */
	public void setSelected(ArrayList<Class<? extends Player>> players) {

		if (players == null) {
			return;
		}
		resizeTable(players.size());
		Iterator<SelectBox<Manager.NamedPlayerClass>> select = botSelectors.iterator();
		int availBotIndex;
		for (Class<? extends Player> bot : players) {
			SelectBox<Manager.NamedPlayerClass> box = select.next();
			availBotIndex = botAvailable(availableBots, bot);
			if (availBotIndex > -1) {
				box.setSelected(availableBots[availBotIndex]);
			}
		}
	}

	/**
	 * Checks if a bot is available in the provided list
	 *
	 * @param bots
	 * @param toSearch
	 * @return
	 */
	private int botAvailable(Manager.NamedPlayerClass[] bots, Class<? extends Player> toSearch) {
		int index = 0;
		for (Manager.NamedPlayerClass bot : bots) {
			if (bot.getClassRef().equals(toSearch)) {
				return index;
			}
			index++;
		}
		return -1;
	}
}

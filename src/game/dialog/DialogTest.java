package game.dialog;

import java.util.Arrays;
import java.util.Scanner;

public class DialogTest extends NPCDialog {

	private static DialogTransactor transactor = new DialogTransactor() {

		@Override
		public void initializeDialog(Dialog dialog) {

		}

		@Override
		public void displayOptions(OptionPage page) {

		}

		@Override
		public void displayDialogPage(Page page) {

		}

		@Override
		public void displayInformation(InformationPage page) {

		}

		@Override
		public void exitDialog() {

		}

	};

	public DialogTest() {
		super(transactor, 1);

		player("Hello, how are you?");
		npc("I'm good, I'm a man of RuneScape. The adventerous world of bull crap.");
		player("Oh, really, give me money then!");
		npc("Okay");
		options("Kill the man any way, then take his money", () -> {
			player("I'll kill you fool");
			npc("Oh god please no!");
		}, "Graciously take his money without killing him", () -> {
			player("Thank you very much");
			npc("You're very welcome sir.");
			//after options are selected, continue the dialog within the option runnables
		});
	}

	public static void main(String[] args) {
		DialogTest test = new DialogTest();
		System.out.println("Press 'Enter' to proceed to the next dialog. If there is an option, enter the value of the option you wish to select.");
		System.out.print("\n" + test.continueDialog().text);

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			scanner.nextLine();
			Page page = test.continueDialog();
			if (page == null)
				break;
			System.out.print(page);
			if (page instanceof OptionPage) {
				OptionPage o = (OptionPage) page;
				o.execute(scanner.nextInt());
			}
		}
		scanner.close();
	}

}

package game.dialog;

/**
 * 
 * @author Albert Beaupre
 */
public class NPCDialog extends Dialog {

    private final int npcId;

    /**
     * 
     * @param transactor
     * @param npcId
     */
    public NPCDialog(DialogTransactor transactor, int npcId) {
	super(transactor);
	this.npcId = npcId;
    }

    /**
     * 
     * @param name
     */
    public NPCDialog npc(String text) {
	Page page = new Page(text);
	page.npcId = npcId;
	pages.add(page);
	return this;
    }

    /**
     * 
     * @param name
     */
    public NPCDialog npc(int expression, String text) {
	Page page = new Page(text);
	page.npcId = npcId;
	page.expression = expression;
	pages.add(page);
	return this;
    }
}

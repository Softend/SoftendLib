package kr.ypshop.softend.enums;

public enum Keybind {

    JUMP("key.jump"), SNEAK("key.sneak"), SPRINT("key.sprint"), LEFT("key.left"), RIGHT("key.right"),
    WALK_BACKWARD("key.back"), WALK_FORWARD("key.forward"), ATTACK("key.attack"), PICK_BLOCK("key.pickitem"), USE("key.use"),
    DROP_ITEM("key.drop"), INVENTORY("key.inventory"), SWAP_ITEM("key.swapOffhand"), TABLIST("key.playerlist"), OPEN_CHAT("key.chat"),
    OPEN_COMMAND("key.command");

    private final String identifier;
    Keybind(String string) {
        identifier = string;
    }

    public String getIdentifier() {
        return identifier;
    }

}

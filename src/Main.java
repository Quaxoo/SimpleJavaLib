public class Main {
    public static void main(String[] args) {
        Window wnd = new Window("Test", 1000,800,false);

        TestObject goblin = new TestObject(300,200,new Sprite("C:\\Users\\Max\\Downloads\\goblin.png", 7));
        goblin.addSprite(new Sprite("C:\\Users\\Max\\Downloads\\goblinHovered.png", 7.1f));
        goblin.isScrollable(true);
    }
}
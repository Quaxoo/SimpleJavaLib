public class Main {
    public static void main(String[] args) {
        Window wnd = new Window("Test", 900,600,false);

        TestObject goblin = new TestObject(300,200,new Sprite("C:\\Users\\Max\\Downloads\\goblin.png", 8));
        goblin.addSprite(new Sprite("C:\\Users\\Max\\Downloads\\goblinHovered.png", 8.1f));
    }
}
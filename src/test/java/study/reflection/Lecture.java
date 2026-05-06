package study.reflection;

public class Lecture {
    private final String name;
    private final int price;
    private boolean visible;

    public Lecture(final String name, final int price) {
        this.name = name;
        this.price = price;
        this.visible = false;
    }

    public Lecture(final String name, final int price, final boolean visible) {
        this.name = name;
        this.price = price;
        this.visible = visible;
    }

    private void changeVisible() {
        this.visible = true;
    }

    @MethodOrder(1)
    public String getName() {
        return this.name;
    }

    @MethodOrder(2)
    public int getPrice() {
        return this.price;
    }
}

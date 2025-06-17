public class Ghost extends Character {
    @Override
    public void attack() {
        System.out.println("The ghost casts a spooky spell.");
    }

    @Override
    public void takeDamage(int points) {
        throw new UnsupportedOperationException("A ghost cannot take physical damage!");
    }
}

public class Warrior extends Character {
    @Override
    public void attack() {
        System.out.println("The warrior strikes with a sword.");
    }

    @Override
    public void takeDamage(int points) {
        System.out.println("The warrior resists and only takes " + (points / 2) + " points of damage.");
    }
}

public class WashingMachine implements MachineActions {

    @Override
    public void turnOn() {
        System.out.println("WashingMachine is ON");
    }

    @Override
    public void turnOff() {
        System.out.println("WashingMachine is OFF");
    }

    @Override
    public void heat() {
        System.out.println("Heat operation does not apply to WashingMachine.");
    }

    @Override
    public void cool() {
        System.out.println("Cool operation does not apply to WashingMachine.");
    }

    @Override
    public void wash() {
        System.out.println("WashingMachine is washing clothes.");
    }
}

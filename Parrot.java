public class Parrot {
    private String name;
    private int health;
    private double crumbs; // Amount of food in kg
    private boolean alive;
    private boolean flying;
    private boolean tamed;

    public Parrot(String name) {
        this.name = name;
        this.health = 3; // Default health is 3 hearts
        this.crumbs = 0.1; // Initial crumbs in the parrot's "stomach"
        this.alive = true;
        this.flying = false;
        this.tamed = false; // Parrot starts untamed
    }

    // Method to feed the parrot
    public void feed(double amount) {
        if (!alive) {
            throw new IllegalStateException("Cannot feed a dead parrot.");
        }
        
        crumbs += amount; // Increase crumbs in the stomach
        health = Math.min(health + 1, 3); // Restore health up to 3

        // Check for overfeeding
        if (crumbs > 2.5) {
            health -= 2; // If overfed, lose 2 health points
            if (health <= 0) {
                alive = false; // Parrot dies if health reaches zero
            }
        }

        // Tame the parrot once it has been fed 1 kg or more cumulatively
        if (crumbs >= 1.0) {
            tamed = true; // Parrot becomes tamed
        }
    }

    // Method to command the parrot (Fly or Stay)
    public void command(String command) {
        if (!alive) {
            throw new IllegalStateException("Cannot command a dead parrot.");
        }
        if (tamed) {
            if (command.equalsIgnoreCase("fly")) {
                flying = true;
            } else if (command.equalsIgnoreCase("stay")) {
                flying = false;
            } else {
                throw new IllegalArgumentException("Invalid command. Use 'fly' or 'stay'.");
            }
        } else {
            throw new IllegalStateException("Parrot must be tamed to follow commands.");
        }
    }

    // Method to hit the parrot (decreases health)
    public void hit() {
        if (!alive) {
            throw new IllegalStateException("Cannot hit a dead parrot.");
        }
        health--;
        if (health <= 0) {
            alive = false; // Parrot dies if health reaches zero
        }
    }

    // Return the current status of the parrot
    @Override
    public String toString() {
        return String.format("%s Parrot %s: %.2f kg crumbs, %d hearts, %s, %s",
                tamed ? "Tamed" : "Untamed", name, crumbs, health, alive ? "Alive" : "Dead", flying ? "Flying" : "Staying");
    }

    // Getters
    public boolean isAlive() {
        return alive;
    }

    public int getHealth() {
        return health;
    }

    public boolean isTamed() {
        return tamed;
    }

    public boolean isFlying() {
        return flying;
    }
}

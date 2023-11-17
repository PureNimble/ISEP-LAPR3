package isep.lapr3.g094.ui.menu;

import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

public class MenuItem {

    private final String description;
    private final Runnable ui;

    public MenuItem(String description, Runnable ui) {
        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("Descrição MenuItem não pode ser null nem vazio.");
        }
        this.description = Objects.requireNonNull(description, "MenuItem não suporta null UI.");
        this.ui = Objects.requireNonNull(ui, "MenuItem não suporta null UI.");
    }

    public void run() {
        this.ui.run();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuItem menuItem = (MenuItem) o;
        return Objects.equals(description, menuItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return this.description;
    }
}
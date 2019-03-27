package rip.titan.archergames.util.structure;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class RegionData {
    public Location getA() {
        return a;
    }

    public void setA(Location a) {
        this.a = a;
    }

    public Location getB() {
        return b;
    }

    public void setB(Location b) {
        this.b = b;
    }

    private Location a, b;
}

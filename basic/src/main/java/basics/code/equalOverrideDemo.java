package basics.code;

import java.util.Objects;

class equalOverrideDemo {
    String name;
    equalOverrideDemo(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof equalOverrideDemo))
            return false;
        equalOverrideDemo other = (equalOverrideDemo) obj;
        return Objects.equals(this.name, other.name);
    }

    public static void main(String[] args) {
        equalOverrideDemo s1 = new equalOverrideDemo("Manoj");
        equalOverrideDemo s2 = new equalOverrideDemo("Manoj");

        System.out.println(s1 == s2);       // false
        System.out.println(s1.equals(s2));
    }
}
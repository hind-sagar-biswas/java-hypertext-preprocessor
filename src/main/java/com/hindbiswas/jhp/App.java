package com.hindbiswas.jhp;

import com.hindbiswas.jhp.engine.FunctionLibrary;
import com.hindbiswas.jhp.engine.JhpEngine;
import com.hindbiswas.jhp.engine.Settings;

class User {
    public String name;
    public int age;
    public String gender;

    User(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        String file = "/home/shinigami/Java/java-hypertext-preprocessor/examples/initial.jhp";

        Settings settings = Settings.builder().base("/home/shinigami/Java/java-hypertext-preprocessor/examples/").build();
        FunctionLibrary lib = new FunctionLibrary();
        JhpEngine engine = new JhpEngine(settings, lib);

        Context ctx = new Context();
        ctx.add("user", new User("Alice", 150, "f"));
        ctx.add("title", "Test JHP");

        String out = engine.render(file, ctx);
        System.out.println(out);
    }
}

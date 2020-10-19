package nz.ac.vuw.ecs.swen225.gp20.persistence;

import nz.ac.vuw.ecs.swen225.gp20.maze.Actor;
import nz.ac.vuw.ecs.swen225.gp20.maze.Direction;
import nz.ac.vuw.ecs.swen225.gp20.maze.Position;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 */
class ActorLoader {

    private List<Class<?>> unknownClasses = new ArrayList<>();
    private Map<String, Class<?>> verifiedClasses = new HashMap<>();

    /**
     * Constructor for the secondary actor loader
     */
    public ActorLoader(){
        initialise();
    }

    /**
     *
     */
    private void initialise(){
        for(File jar : detectJarFiles()){
            try {
                loadClassesFromJar(jar);
            } catch (IOException | ClassNotFoundException ignored) {}
        }
    }

    /**
     * @return An array of any jar files stored in the levels folder
     */
    private File[] detectJarFiles(){
        File jarFolder = new File("levels");
        return jarFolder.listFiles(((dir, name) ->
                name.matches("^level[0-9]+\\.jar$")));
    }

    /**
     * @param jar
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void loadClassesFromJar(File jar) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + jar.toPath()+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }

            String className = je.getName().substring(0,je.getName().length()-6); // -6 because of .class
            className = className.replace('/', '.');
            unknownClasses.add(cl.loadClass("nz.ac.vuw.ecs.swen225.gp20.maze." + className));
        }
    }

    private void verifyClasses(){
        for (Class<?> c : unknownClasses){
            Object o = null;
            try {
                o = c.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException |
                    NoSuchMethodException | IllegalAccessException ignored) {}
            if(o instanceof Actor){
                verifiedClasses.put(((Actor) o).getCode(), c);
            }
        }
    }

    /**
     * Returns an Actor object
     * @param code
     * @param n
     * @param p
     * @param path
     * @return
     */
    public Actor createActorInstance(String code, String n, Position p, List<Direction> path){
        Object o = null;
        try{
            o = verifiedClasses.get(code).getDeclaredConstructor(p.getClass(),
                    n.getClass(), path.getClass()).newInstance(p, n, path);
        } catch (InstantiationException | InvocationTargetException
                | NoSuchMethodException | IllegalAccessException ignored) {}
        return (Actor) o;
    }

    public static void main(String[] args){

    }
}

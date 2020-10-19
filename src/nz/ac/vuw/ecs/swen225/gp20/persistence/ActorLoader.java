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
public class ActorLoader {

    private Map<Integer, List<Class<?>>> unknownClasses = new HashMap<>();
    private Map<Integer, Map<String, Class<?>>> verifiedClasses = new HashMap<>();
    private boolean requiredForThisLevel = false;

    /**
     * Constructor for the secondary actor loader
     */
    public ActorLoader(List<Integer> allLoadedLevelNumbers){
        initialise(allLoadedLevelNumbers);
    }

    private void initialise(List<Integer> levelNumbers){
        for(int num : levelNumbers){
            for(File jar : detectMatchingJarFile(num)){
                try {
                    storeUnknownClasses(num, jar);
                } catch (IOException | ClassNotFoundException ignored) {}
            }
            verifyClasses();
        }
    }

    /***
     *
     */
    private File[] detectMatchingJarFile(int levelNumber){
        File jarFolder = new File("levels");
        return jarFolder.listFiles(((dir, name) ->
                name.matches("level" + String.valueOf(levelNumber) + ".json")));
    }

    /**
     * @param jar
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void storeUnknownClasses(int levelNumber, File jar) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + jar.toPath()+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        unknownClasses.put(levelNumber, new ArrayList<>());
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }

            String className = je.getName().substring(0,je.getName().length()-6); // -6 because of .class
            className = className.replace('/', '.');
            unknownClasses.get(levelNumber).add(cl.loadClass("nz.ac.vuw.ecs.swen225.gp20.maze." + className));
        }
    }

    private void verifyClasses(){
        for (Integer levelNumber : unknownClasses.keySet()){
            for(Class<?> unverifiedClass : unknownClasses.get(levelNumber)) {
                Object o = null;
                try {
                    o = unverifiedClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | InvocationTargetException |
                        NoSuchMethodException | IllegalAccessException ignored) {
                }
                if (o instanceof Actor) {
                    if(!verifiedClasses.containsKey(levelNumber)){
                        verifiedClasses.put(levelNumber, new HashMap<>());
                    }
                    verifiedClasses.get(levelNumber).put(((Actor) o).getCode(), unverifiedClass);
                    requiredForThisLevel = true;
                }
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
    public Actor createSecondaryActor(Integer levelNumber, String code, String n, Position p, List<Direction> path){
        Object o = null;
        try{
            o = verifiedClasses.get(levelNumber).get(code).getDeclaredConstructor(p.getClass(),
                    n.getClass(), path.getClass()).newInstance(p, n, path);
        } catch (InstantiationException | InvocationTargetException
                | NoSuchMethodException | IllegalAccessException ignored) {}
        return (Actor) o;
    }

    public boolean isRequiredForThisLevel(){
        return requiredForThisLevel;
    }
}

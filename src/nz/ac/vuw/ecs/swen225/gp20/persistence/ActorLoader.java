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

    /**
     * Classes that have been fetched from a jar file
     */
    private final Map<Integer, List<Class<?>>> unknownClasses = new HashMap<>();

    /**
     * Classes that have been verified as instances of Actor, stored first by level number,
     * then by String code.
     */
    private final Map<Integer, Map<String, Class<?>>> verifiedClasses = new HashMap<>();

    /**
     * Constructor for the secondary actor loader
     *
     * @param allLoadedLevelNumbers a list of the levels found (by LevelLoader)
     */
    public ActorLoader(List<Integer> allLoadedLevelNumbers){

        initialise(allLoadedLevelNumbers);
    }

    /**
     * Find all classes stored in the jar files and verify they are valid
     * Actor subclasses.
     *
     * @param levelNumbers a list of the levels found (by LevelLoader)
     */
    private void initialise(List<Integer> levelNumbers){
        for(int num : levelNumbers){
            for(File jar : detectMatchingJarFile(num)) {
                try {
                    storeUnknownClasses(num, jar);
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
        }
        verifyClasses();
    }


    /**
     * Find a jar file that matches the given level
     *
     * @param levelNumber the level number to check for jars
     * @return a list of jar files that relate to the level
     */
    private File[] detectMatchingJarFile(int levelNumber){
        File jarFolder = new File("levels");
        return jarFolder.listFiles(((dir, name) ->
                name.matches("level" + String.valueOf(levelNumber) + ".jar")));
    }

    /**
     * Loop through the classes stored in a given jar file and store
     * them by level number for later verification.
     *
     * @param jar the jar file that may hold secondary actors
     *
     * @throws IOException cannot read the jar file
     * @throws ClassNotFoundException the secondary Actor class cannot be loaded
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

    /**
     * This instantiates an object of an unverified class and checks it
     * is an instance of Actor, then stores it by level number
     */
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
                }
            }
        }
    }

    /**
     *
     * @param levelNumber
     * @return
     */
    public Set<Actor> getSetOfSecondaryActors(int levelNumber, LevelLoader levelLoader){
        Set<Actor> actors = new HashSet<>();
        Map<String, String> actorNames = levelLoader.getLevelActorNames(levelNumber);
        Map<String, List<Position>> actorPositions = levelLoader.getLevelActorPositions(levelNumber);
        Map<String, List<List<Direction>>> actorPaths = levelLoader.getLevelActorPaths(levelNumber);
        for(String code : actorNames.keySet()){
            for(int i = 0; i < actorPositions.get(code).size(); i++){
                Actor secondaryActor = createSecondaryActor(levelNumber, code,
                        actorNames.get(code), actorPositions.get(code).get(i), actorPaths.get(code).get(i));
                actors.add(secondaryActor);
            }
        }
        return actors;
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
            o = verifiedClasses.get(levelNumber).get(code).getDeclaredConstructor(Position.class,
                    String.class, List.class).newInstance(p, n, path);
        } catch (InstantiationException | InvocationTargetException
                | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return (Actor) o;
    }

    /**
     * @param levelNumber
     * @return
     */
    public boolean isRequiredForThisLevel(int levelNumber){
        return verifiedClasses.containsKey(levelNumber) && !verifiedClasses.get(levelNumber).isEmpty();
    }
}

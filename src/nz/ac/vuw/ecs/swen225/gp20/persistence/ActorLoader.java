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
     * Stores classes pulled from jar files in the levels directory, by level number.
     */
    private final Map<Integer, List<Class<?>>> unknownClasses = new HashMap<>();
    /**
     * Stores classes that are verified as subtypes of Actor by level number, then
     * by String code.
     */
    private final Map<Integer, Map<String, Class<?>>> verifiedClasses = new HashMap<>();

    /**
     * Constructor for the secondary actor loader
     * @param allLoadedLevelNumbers the levels loaded successfully by
     */
    public ActorLoader(List<Integer> allLoadedLevelNumbers){

        initialise(allLoadedLevelNumbers);
    }

    /**
     * Stores any classes found in jar files, then verifies them as
     * valid secondary actor classes in a field.
     *
     * @param levelNumbers the levels already loaded
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
     * Find jar files with the correct name in the levels directory
     * @param levelNumber a specific level number
     * @return an Array of matching jar files
     */
    private File[] detectMatchingJarFile(int levelNumber){
        File jarFolder = new File("levels");
        return jarFolder.listFiles(((dir, name) ->
                name.matches("level" + levelNumber + ".jar")));
    }

    /**
     * Store any classes found in the jar files
     * @param levelNumber A level number
     * @param jar A jar file from levels directory
     * @throws IOException If cannot access the jar file
     * @throws ClassNotFoundException If class cannot be created
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
     *
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
     * Create the secondary actors stored in the Level file
     * @param levelNumber A level number
     * @param levelLoader the levelLoader object
     * @return a Set of Actors
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
     *
     * @param levelNumber a Level number
     * @param code the single character String representation of an Actor
     * @param name the Actor name
     * @param position the Actor position
     * @param path the Actor's path of Directions
     * @return a Actor object
     */
    public Actor createSecondaryActor(Integer levelNumber, String code, String name,
                                      Position position, List<Direction> path){
        Object o = null;
        try{
            o = verifiedClasses.get(levelNumber).get(code).getDeclaredConstructor(Position.class,
                    String.class, List.class).newInstance(position, name, path);
        } catch (InstantiationException | InvocationTargetException
                | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return (Actor) o;
    }

    /**
     * Checks if secondary actors classes were found for a given level.
     * @param levelNumber a Level number
     * @return True if imported classes were found.
     */
    public boolean isRequiredForThisLevel(int levelNumber){
        return verifiedClasses.containsKey(levelNumber) && !verifiedClasses.get(levelNumber).isEmpty();
    }
}

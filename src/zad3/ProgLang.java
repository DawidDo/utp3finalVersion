package zad3;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProgLang  {

    private Map<String, Set<String>> progMap;
    private Map<String,Set<String>> langMap;
    private List<String> allLines;
    private Set <String> progsSet;
    private List<String> langsList;
    public ProgLang(String str){
        langsList = new ArrayList();
        progsSet = new LinkedHashSet();
        langMap = new LinkedHashMap();
        progMap = new LinkedHashMap();
        allLines = new ArrayList();
        try{
            List<String> allLines = Files.readAllLines(Paths.get(str), Charset.defaultCharset());
            langsList =Pattern.compile("\\s").splitAsStream(allLines.stream().collect(Collectors.joining(" "))).filter(e->e.length() > 1).collect(Collectors.toList());
            List<List<String>> progsList = new ArrayList();
            String[] progs = allLines.stream().toArray(String[]::new);
            for (int i = 0 ; i < progs.length ; i++) {
                String newLine = progs[i].replaceAll("[A-Za-z]{2,}", "").replace("C++","").replace("C#","").replaceAll("\\t","");
                String [] myLine = newLine.split("|\\n");
                progsList.add(Arrays.asList(myLine));
            }
            Set<Set<String>> setProg = new LinkedHashSet<>();

            for(List<String> list : progsList){
                setProg.add(new LinkedHashSet(list));
            }

            Iterator<String> lang = langsList.iterator();
            Iterator<Set<String>> prog = setProg.iterator();

            while (lang.hasNext() && prog.hasNext()) {
                langMap.put(lang.next(), prog.next());
            }

            for(Set<String> set : setProg){
                progsSet.addAll(set);
            }

            for(String  lg : langMap.keySet() ){
                    for(String  pr : langMap.get(lg)){

                    if(progMap.containsKey(pr)){
                        progMap.get(pr).add(lg);}
                    else{
                        Set<String> lag = new LinkedHashSet<>();
                        lag.add(lg);
                        progMap.put(pr,lag);
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Błąd " + e);
        }
    }

    public Map<String, Set<String>> getLangsMap(){

        return langMap; }

    public Map<String,Set<String>>getProgsMap(){

        return progMap;}

    public Map <String,LinkedHashSet<String>>getLangsMapSortedByNumOfProgs(){
        LinkedHashMap sortedBy = sorted(langMap, Collections.reverseOrder((Comparator<Map.Entry<String, Set<String>>>) (a, b) -> {
            int i = Integer.compare(a.getValue().size(),b.getValue().size());
            if (i != 0) {
                return i;
            }else{
               List<String> list1 = new ArrayList<>(Arrays.asList(a.getKey(),b.getKey()));
               list1.sort(Comparator.comparing(Object::toString));

                if(list1.get(0).equals(a.getKey()))
                    return 1;
                else if(list1.get(0).equals(b.getKey()))
                    return -1;
                else
                    return 0;
            }
        }));
    return sortedBy;}

    public LinkedHashMap<String, Set<String>> getProgsMapSortedByNumOfLangs(){
        LinkedHashMap linkedHashMap = sorted(progMap, Collections.reverseOrder((Comparator<Map.Entry<String,Set<String>>>) (a, b) -> {
            int i = Integer.compare(a.getValue().size(),b.getValue().size());
            if (i != 0)
                return i;
            else
                return Character.compare(b.getKey().charAt(0), a.getKey().charAt(0));
        }));
        return linkedHashMap;}

    public LinkedHashMap <String,Set<String>> getProgsMapForNumOfLangsGreaterThan(int arg){
        LinkedHashMap linkedHashMap = filter(progMap, (Predicate<Map.Entry<String, Set<String>>>) e -> e.getValue().size() > arg);

        return linkedHashMap; }

        static LinkedHashMap filter(Map map, Predicate predicate){
        LinkedHashMap linkedHashMap = new LinkedHashMap<>();

       map.entrySet().stream().filter(predicate).forEach(e->linkedHashMap.put(((Map.Entry) e).getKey(),((Map.Entry) e).getValue()));
        return linkedHashMap;
    }

         static LinkedHashMap sorted(Map map, Comparator comparator){
            LinkedHashMap linkedHashMap = new LinkedHashMap<>();

            map.entrySet().stream().sorted(comparator).forEach(e-> linkedHashMap.put(((Map.Entry) e).getKey(),((Map.Entry) e).getValue()));
            return linkedHashMap;
        }
}

package br.unb.cic.qm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Solving the problem of shuffled couples.
 * 
 * @author grodrigues
 *
 */
public class CouplesShuffle {
	
	List<String> persons;
	
	List<Counter> counters;
	
	public static void main (String[] args){

		CouplesShuffle instance = new CouplesShuffle();
		instance.run();

	}
	
	public CouplesShuffle(){
		// 4 couples: h1, m1 is a couple
		persons = Arrays.asList( "h1","h2","h3","h4","m1","m2","m3","m4" );
		
		counters = new ArrayList<Counter>();
		
		counters.add(new Universe());
		counters.add(new JimAndPaulaAtLeft());
		counters.add(new JimAndPaulaSideBySide());
		counters.add(new CoupleSideBySide());
	}
	
	private void run(){	
		List<String> places = new ArrayList<String>();
		
		sitOneByOneAndCount(persons, places);
		printRestult();
	}
	
	private void randomSitAndCount(){
		
		List<String> places = new ArrayList<String>();
	
		//shuffle and count, over and over
		for(int j = 0; j < 1000000; j++){
			places.clear();
			places.addAll(persons);
			Collections.shuffle(places);
			count(places);
		}
	}
	
	public void sitOneByOneAndCount(List<String> standing, List<String> sited) {
		if(standing.size() == 0){
			count(sited);
			return;
		}
		else{ 
			for(String sitting: standing){
				List<String> newStanding = new ArrayList<>();
				newStanding.addAll(standing);
				newStanding.remove(sitting);
				
				List<String> newSitted = new ArrayList<>();
				newSitted.addAll(sited);
				newSitted.add(sitting);
				sitOneByOneAndCount(newStanding, newSitted);
			}
		}
	}

	private void count(List<String> sits) {
		for(Counter counter: this.counters){
			counter.count(sits);
		}
	}
	
	private void printRestult() {
		for(Counter counter: this.counters){
			System.out.print(counter.getName() + ",");
		}
		System.out.println();
		for(Counter counter: this.counters){
			System.out.print(counter.getMatchs()+ ",");
		}
	}

	public abstract class Counter {
		protected String eventName = null;
		
		long matchs = 0;
		
		Counter(String name){
			this.eventName = name;
		}
		
		public abstract void count(List<String> sits );
		
		public long getMatchs(){
			return matchs;
		}
		
		public String getName(){
			return this.eventName;
		}
		
	}
	

	

	/**
	 * Count every event
	 * @author grodrigues
	 *
	 */
	public class Universe extends Counter{
		Universe(){
			super("universe");
		}
		
		public void count(List<String> sits ){
			matchs++;
		}
	}
	
	/**
	 * Count Jim And Paula at Left, event
	 *  qual a probabilidade que Jim e Paula (casados) sentem nas duas 
	 * poltronas no canto esquerdo?
	 */
	public class JimAndPaulaAtLeft extends Counter{
		JimAndPaulaAtLeft(){
			super("JimAndPaulaAtLeft");
		}
		 
		public void count(List<String> sits ){
			if(isSideBySide("h1", "m1", sits.get(0), sits.get(1))){
				matchs++;
			}
		}
	}
	
	/**
	 * Count Jim And Paula side by side event
	 * Qual a probabilidade que Jim e Paula se sentem um do lado do outro?
	 */
	public class JimAndPaulaSideBySide extends Counter {
		JimAndPaulaSideBySide(){
			super("JimAndPaulaSideBySide");
		}
		public void count(List<String> sits ){
			for(int i = 0; (i +1) < sits.size(); i++ ){
				if(isSideBySide("h1", "m1", sits.get(i), sits.get(i+1))){
					matchs++;
				}
			}
		}
	}
	
	/**
	 * Count at least one couple side by side event
	 * "Qual a probabilidade de que  pelo menos uma esposa 
	 * se sente ao lado de seu esposo?"
	 */
	public class CoupleSideBySide extends Counter {
		CoupleSideBySide(){
			super("CoupleSideBySide");
		}
		public void count(List<String> sits ){
			for(int i = 0; (i +1) < sits.size(); i++ ){
				if(isSideBySide("h1", "m1", sits.get(i), sits.get(i+1))){
					matchs++;
					return;
				}else if(isSideBySide("h2", "m2", sits.get(i), sits.get(i+1))){
					matchs++;
					return;
				}else if(isSideBySide("h3", "m3", sits.get(i), sits.get(i+1))){
					matchs++;
					return;
				}else if(isSideBySide("h4", "m4", sits.get(i), sits.get(i+1))){
					matchs++;
					return;
				}
			}
		}
	}
	
	public boolean isSideBySide(String personA, String personB, String placeA, String placeB ){
		if(personA.equals(placeA)
		&& personB.equals(placeB)
		||	
		(personB.equals(placeA)
		&& personA.equals(placeB))){
			return true;
		}else{
			return false;
		}
	}
}


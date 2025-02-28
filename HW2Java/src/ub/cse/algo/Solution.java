package ub.cse.algo;

import java.util.*;

/**
 * For use in CSE 331 HW1.
 * This is the class you will be editing and turning in. It will be timed against our implementation
 * NOTE that if you declare this file to be in a package, it will not compile in Autolab
 */

public class Solution {
	private int _nHospital;
	private int _nStudent;

    // The following represent the preference list of hospitals and students.
    // The KEY represents the integer representation of a given hospital or student.
    // The VALUE is a list, from most preferred to least.
    // For hospital, first element of the list is number of available slots
	private HashMap<Integer, ArrayList<Integer>> _hospitalList;
	private HashMap<Integer, ArrayList<Integer>> _studentList;
    
    
    /**
     * The constructor simply sets up the necessary data structures.
     * The grader for the homework will first call this class and pass the necessary variables.
     * There is no need to edit this constructor.
     * @param m Number of hospitals
     * @param n Number of students
     * @param hospitalList A map linking each hospital with its preference list
     * @param studentList A map linking each student with their preference list
     * @return
     */
	public Solution(int m, int n, HashMap<Integer, ArrayList<Integer>> hospitalList, HashMap<Integer, ArrayList<Integer>> studentList) {
		_nHospital = m;
		_nStudent = n;
		_hospitalList = hospitalList;
		_studentList = studentList;
	}
    
    /**
     * This method must be filled in by you. You may add other methods and subclasses as you see fit,
     * but they must remain within the HW1_Student_Solution class.
     * @return Your stable matches
     */
	public ArrayList<Match> getMatches() {

		//arraylist init
		ArrayList<Match> matches = new ArrayList<>();
		ArrayList<Integer> studentPref;
		ArrayList<Integer> hospPref;

		//hashmap init
		HashMap<Integer, HashMap<Integer, Integer>> hospitalConsiders = new HashMap<>();
		HashMap<Integer, HashSet<Integer>> studentProposed = new HashMap<>();
		HashMap<Integer, HashMap<Integer,Integer>> hospRanks = new HashMap<>();
		HashMap<Integer,Integer> consider;

		//queue init
		Queue<Integer> proposing = new LinkedList<>(_studentList.keySet());

		//int init
		int student;
		int rank;
		int largestNum;
		int hospSlots;

		while (!proposing.isEmpty()) {

			student = proposing.poll();

			studentPref = _studentList.get(student);

			if (!studentProposed.containsKey(student)) {
				studentProposed.put(student, new HashSet<>());
			}

			for (int hospital: studentPref) { //loops through student hospital list

				if (studentProposed.get(student).contains(hospital)) //if this student alr proposed to hospital skip
					continue;
				if (!hospitalConsiders.containsKey(hospital))
					hospitalConsiders.put(hospital,new HashMap<>());

				consider = hospitalConsiders.get(hospital);

				hospPref = _hospitalList.get(hospital);
				studentProposed.get(student).add(hospital); //proposing to first hospital

				if (!hospRanks.containsKey(hospital)) {
					HashMap<Integer, Integer> studentRanks = new HashMap<>();
					for (int i = 0; i < hospPref.size(); i++) {
						studentRanks.put(hospPref.get(i), i);
					}
					hospRanks.put(hospital,studentRanks);
				}

				hospSlots = hospPref.get(0);
				if (hospSlots != 0) //as long as there are slots available hospitals will consider a student
				{
					consider.put(hospRanks.get(hospital).get(student), student);
					hospPref.set(0,hospSlots-1);
					break;
				}
				else //when there is no more slots the hospital must check to see who they would prefer if this student is rejected they must search
				{ //through their pref list of hospitals until one will take them

					rank = hospRanks.get(hospital).get(student);
					largestNum = Collections.max(consider.keySet()); //found in source Java Oracle Documentation for java.collection

					if (rank < largestNum) //when current student proposal is more desirable for hospital
					{
						proposing.add(consider.remove(largestNum));
						consider.put(rank,student);
						break;
					}

				}
			}
		}

		for (int k : hospitalConsiders.keySet())
		{
			for (int s : hospitalConsiders.get(k).values())
				matches.add(new Match(k,s));
		}

        return matches;
	}

}

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

		ArrayList<Match> matches = new ArrayList<>();
		HashMap<Integer, HashMap<Integer, Integer>> hospitalConsiders = new HashMap<>();
		HashMap<Integer, HashSet<Integer>> studentProposed = new HashMap<>();
		boolean breakCheck = false;

		int hospitalSlots; // slots that hospitals have
		Queue<Integer> proposing = new LinkedList<>(_studentList.keySet());
		ArrayList<Integer> studentPref = new ArrayList<>();
		ArrayList<Integer> hospPref = new ArrayList<>();

		while (!proposing.isEmpty()) {
			int student = proposing.poll();
			studentPref = _studentList.get(student);
			if (!studentProposed.containsKey(student)) {
				studentProposed.put(student, new HashSet<>());
			}

			for (int hospital: studentPref) { //loops through student hospital list
				if (studentProposed.get(student).contains(hospital)) //if this student alr proposed to hospital skip
					continue;
				if (!hospitalConsiders.keySet().contains(hospital))
					hospitalConsiders.put(hospital,new HashMap<>());

				hospPref = _hospitalList.get(hospital);
				studentProposed.get(student).add(hospital); //proposing to first hospital

				if (hospitalConsiders.get(hospital).size() < hospPref.get(0)) //as long as there are slots available hospitals will consider a student
				{
					hospitalConsiders.get(hospital).put(hospPref.indexOf(student), student);
					break;
				}
				else //when there is no more slots the hospital must check to see who they would prefer if this student is rejected they must search
				{ //through their pref list of hospitals until one will take them

					int rank = hospPref.indexOf(student);

					int largestNum = 0; //find a way to store largest num for each hospital...
					for (int k : hospitalConsiders.get(hospital).keySet()) {
						if (largestNum < k) {
							largestNum = k;
						}
					}
					if (rank < largestNum) //when current student proposal is more desirable for hospital
					{
						proposing.add(hospitalConsiders.get(hospital).remove(largestNum));
						hospitalConsiders.get(hospital).put(rank,student);
						break;
					}

				}
			}

		}

		for (int k : hospitalConsiders.keySet())
		{
			for (int student : hospitalConsiders.get(k).values())
				matches.add(new Match(k,student));
		}

        return matches;
	}
}

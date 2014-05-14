package com.dabomstew.pkrandom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import com.mishadoff.randomorg.IntegerGenerator;

/*----------------------------------------------------------------------------*/
/*--  RandomSource.java - functions as a centralized source of randomness	--*/
/*--  			     	  to allow the same seed to produce the same random --*/
/*--					  ROM consistently.           						--*/
/*--  																		--*/
/*--  Part of "Universal Pokemon Randomizer" by Dabomstew					--*/
/*--  Pokemon and any associated names and the like are						--*/
/*--  trademark and (C) Nintendo 1996-2012.									--*/
/*--  																		--*/
/*--  The custom code written here is licensed under the terms of the GPL:	--*/
/*--                                                                        --*/
/*--  This program is free software: you can redistribute it and/or modify  --*/
/*--  it under the terms of the GNU General Public License as published by  --*/
/*--  the Free Software Foundation, either version 3 of the License, or     --*/
/*--  (at your option) any later version.                                   --*/
/*--                                                                        --*/
/*--  This program is distributed in the hope that it will be useful,       --*/
/*--  but WITHOUT ANY WARRANTY; without even the implied warranty of        --*/
/*--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the          --*/
/*--  GNU General Public License for more details.                          --*/
/*--                                                                        --*/
/*--  You should have received a copy of the GNU General Public License     --*/
/*--  along with this program. If not, see <http://www.gnu.org/licenses/>.  --*/
/*----------------------------------------------------------------------------*/

public class RandomSource {
	
	// randoms generated from this come from random.org
	private static IntegerGenerator ig = new IntegerGenerator();
	
	// this is the backup local random instance in case we get denied
	private static Random backupSource = new Random();
	
	// this is a bunch of backup seeds fetched at the start in case random.org denies us
	private static ArrayList<Integer> backupSeeds = new ArrayList<Integer>();
	
	// this value will be used to adjust the frame in which the local random backup is reseeded
	private static int reseedTimeout;
	
	static int calls = 0;
	static int backupCalls = 0;
	
	public static RandomSourceInstance instance;
	
	// this instance is used in the collections shuffle
	private static class RandomSourceInstance extends Random
	{
		public int nextInt(int n)
		{
			return RandomSource.nextInt(n);
		}		
	}

	public static int nextInt(int randomSize) {
		calls++;
//		
//		try {
//			return ig.generate(0, randomSize-1, 1).get(0);
//		}
//		catch (IOException e) {
			incrementBackupCalls();
			return backupSource.nextInt(randomSize);
//		}
	}

	private static void incrementBackupCalls() 
	{
//		System.out.println("There was an error retrieving a value");
//		System.out.println("Getting a backup value from a randomly seeded local instance");
		
		backupCalls++;
		
		if (backupCalls > reseedTimeout)
			setBackupSourceSeed();
	}

	private static void setBackupSourceSeed() {
		backupCalls = 0;
		int removedSeed = backupSeeds.remove(0);
		backupSeeds.add(removedSeed); // re add this random seed to the end
		
		// reseed this
		backupSource.setSeed(removedSeed);
		
	}

	public static double nextDouble() {
		calls++;
		
//		try {
//			return ((double)ig.generate(0, 1000000000-1, 1).get(0)) / 1000000000;
//		} 
//		catch (IOException e) {
			incrementBackupCalls();
			return backupSource.nextDouble();
//		}
	}

	public static double random() {
		return nextDouble();
	}

	public static Random instance() {
		return instance;
	}

	public static void seed(long seed) {
		calls = 0;
		ig = new IntegerGenerator();
				
		// fetch our backup seeds for if the generator denies us
		try {
			ArrayList<Integer> lengthAndTimeout = ig.generate(2, 1000, 2);
			int length = lengthAndTimeout.remove(0);
			reseedTimeout = lengthAndTimeout.remove(0)/3;
			
			backupSeeds = ig.generate(0, 1000000000, length);
		}
		catch (IOException e) {
			System.out.println("Can't even get backup seeds, random.org is rejecting everything.");
			System.out.println("Perhaps try getting a new IP address? We will need at least some initial seeds.");
			JOptionPane.showMessageDialog(null, "Error: Could not get random information from random.org. Are you online?");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
			// rage quit
			System.exit(0);
		}
	
		// and set the backup source seed
		setBackupSourceSeed();
		
	}

	public static long pickSeed() {
		// this will never do anything as we don't really have seeds
		return 27;
	}

	public static int callsSinceSeed() {
		return calls;
	}

}

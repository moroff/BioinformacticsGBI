package com.gbi; /**
 * You do not need to change this file.
 * 
 * A FastA object holds DNAsequence objects. 
 * 
 * for GBI, 29.4.2021
 */
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;

public class FastA {

	List<DNAsequence> sequences = new ArrayList<DNAsequence>();

	public FastA() {
	}

	/**
	 * 
	 * @param index
	 * @return i-th sequence
	 */
	public String getSequence(int index) {
		return this.sequences.get(index).sequence;
	}
	
	/**
	 * 
	 * @param index
	 * @return i-th sequence name
	 */
	public String getSequenceName(int index) {
		return this.sequences.get(index).header;
	}
	
	/**
	 * 
	 * @return number of sequences
	 */
	public int size() {
		return this.sequences.size();
	}

	/**
	 * Adds sequences of a fasta file to the object.
	 * @param reader
	 */
	public void read(BufferedReader reader) {

		String line = null;
		String sequence = "";
		String header = null;

		try {

			while ((line = reader.readLine()) != null) {

				if (line.startsWith(">")) {

					if (header == null) {
						header = line.substring(1);

					} else {
						this.sequences.add(new DNAsequence(header, sequence));
						header = line.substring(1);
						sequence = "";
					}

				} else {
					sequence += line;
				}

			}
			if (header != null) {
				this.sequences.add(new DNAsequence(header, sequence));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

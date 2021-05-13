package com.gbi.assignment_1;//Made by Friederike Moroff & Gwendolyn Gusak

// import the ArrayList package

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ReadingAndWriting_FASTA {
    static final String LINEEND = System.lineSeparator();
    static class Sequence {
        String header;
        String sequence;
        String reverseComplement;
        boolean valid;
    }
    List<Sequence> sequences = new ArrayList<>();

    public void addSequence(String header, String sequence) {
        Sequence seq = new Sequence();
        seq.header = header;
        seq.sequence = sequence;
        sequences.add(seq);
    }


    // reads sequences from a single or multi FASTA file
    public void readFasta(String file) {
        Sequence currentSequence = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine = reader.readLine();

            while (currentLine != null) {
                currentLine = currentLine.strip();
                if (currentLine.length() != 0) {
                    if (currentLine.startsWith(">")) {
                        currentSequence = new Sequence();
                        sequences.add(currentSequence);
                        currentSequence.header = currentLine;
                    } else {
                        String sequencePart = currentLine;
                        if (currentSequence.sequence == null) {
                            currentSequence.sequence = sequencePart;
                        } else {
                            currentSequence.sequence = currentSequence.sequence + sequencePart;
                        }
                    }
                }
                currentLine = reader.readLine();
            }
            reader.close();

            sequences.forEach((s) -> {
                s.valid = validateSequence(s.sequence);
                if (!s.valid) {
                    System.out.println(s.header + " invalid sequence");
                }
            });
        }

        catch (Exception ex) {
            System.out.println("Error! File not found!");
        }
    }

    // checks if the sequence is a valid nucleotid/amino acid sequence
    public static boolean validateSequence(String sequence) {
        boolean sequenceValidated = true;
        Set<Character> nucleotides = Set.of('A', 'C', 'G', 'T');
        Set<Character> aminoAcids = Set.of('A', 'R', 'N', 'D', 'C', 'Q', 'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V', 'U', 'O');
        for(int i = 0; i < sequence.length(); i++) {
            char currentCharInSequence = sequence.charAt(i);
            if (! nucleotides.contains(currentCharInSequence)) {
                sequenceValidated = false;
            }
        }
        if (!sequenceValidated) {
            sequenceValidated = true;
            for (int i = 0; i < sequence.length(); i++) {
                char currentCharInSequence = sequence.charAt(i);
                if (! aminoAcids.contains(currentCharInSequence)){
                    sequenceValidated = false;
                }
            }
        }

        return sequenceValidated;
    }

    // outputs the length of the read sequences
    public void printSequenceLength() {
        for (Sequence sequence: sequences) {
            System.out.println(" The sequence " + sequence.header + " is " + sequence.sequence.length() + " long.");
        }
    }

    // writes sequences to a single or multi FASTA file
    public void write(String outputFile, boolean complement, boolean reverseOrder, boolean length) {
        try {
            Writer writer = new FileWriter(outputFile);
            ListIterator<Sequence> it = sequences.listIterator(reverseOrder? sequences.size() : 0);

            do {
                Sequence sequence = reverseOrder ? it.previous() : it.next();
                if (sequence.valid) {
                    writer.append(sequence.header);
                    if (length) {
                        writer.append(" length = ").append(Integer.toString(sequence.sequence.length()));
                    }
                    writer.append(LINEEND);
                    String seq = complement ? sequence.reverseComplement : sequence.sequence;
                    int l = seq.length();
                    for (int i = 0; i < l; i += 80) {
                        writer.append(seq.substring(i, Math.min(l, i + 80))).append(LINEEND);
                    }
                } else {
                    System.out.println(sequence.header + " has not a valid sequence");
                }
            } while ( reverseOrder ? it.hasPrevious() : it.hasNext() );
            writer.close();
        } catch (Exception ex) {
            System.out.println("error! String not found!");
        }
    }

    // creates the reverse complement sequence for nucleotide sequences
    public void calculateReverseComplement() {
        for (Sequence sequence : sequences) {
            if (sequence.valid) {
                StringBuilder sb = new StringBuilder(sequence.sequence.length());
                for (int i = sequence.sequence.length() - 1; i >= 0; --i) {
                    sb.append(complement(sequence.sequence.charAt(i)));
                }
                sequence.reverseComplement = sb.toString();
            }
        }
    }

    // creates the complement sequence for nucleotide sequences
    private char complement(char substring) {
        switch (substring) {
            case 'A': return 'T';
            case 'T': return 'A';
            case 'C': return 'G';
            case 'G': return 'C';
            default: throw new IllegalArgumentException(substring + " not a valid base");
        }
    }

    public static void main(String[] args) {
        ReadingAndWriting_FASTA fasta = new ReadingAndWriting_FASTA();
        fasta.readFasta("./material/Data1-single.fasta");
        fasta.printSequenceLength();
        fasta.calculateReverseComplement();
        fasta.write("./material/revcomp_fasta.fasta", true, false, true);  // reverse complement of Data1

        ReadingAndWriting_FASTA fasta2 = new ReadingAndWriting_FASTA();
        fasta2.readFasta("./material/Data2-multiple.fasta");
        fasta2.printSequenceLength();
        fasta2.write("./material/revorder_fasta2.fasta", false, true, true);  // reverse order of Data2
    }
}

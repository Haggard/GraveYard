package com.gmail.Haggard_nl.GraveYard.Conversations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PossibleAnswers
{
    static String[] approvals = { "yes", "sure", "yeah", "why not", "possibly", "ok", "k", "kk", "of cause", "no problem", "yep", "yip", "yea" };
    static String[] disapprovals = { "no", "never", "nope", "not", "pff", "i can't" };
    static String[] considering = { "well", "don't know", "not sure", "maybe", "i dont have", "probably", "sorry" };

  public static final Set<String> approvalWords = new HashSet(Arrays.asList(approvals));
  public static final Set<String> disapprovalWords = new HashSet(Arrays.asList(disapprovals));
  public static final Set<String> considerationWords = new HashSet(Arrays.asList(considering));

}
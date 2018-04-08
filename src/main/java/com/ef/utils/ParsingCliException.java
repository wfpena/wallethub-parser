package com.ef.utils;

public class ParsingCliException extends Exception{
  public ParsingCliException(String message)
  {
    super("\n\tParsing arguments - " + message);
  }
}


package com.ef.utils;

/*
 * Custom Exception for the parser
 */
public class ParsingCliException extends Exception{
  public ParsingCliException(String message)
  {
    super("\n\tParsing arguments - " + message);
  }
}


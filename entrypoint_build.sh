#!/usr/bin/env bash
rm -rvf /javafxbinary/*
cp -vf /javafxlibrary-*-jar-with-dependencies.jar /javafxbinary/.
chmod 555 /javafxbinary/javafxlibrary-*-jar-with-dependencies.jar
java -jar /javafxlibrary-*-jar-with-dependencies.jar

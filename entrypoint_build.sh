#!/usr/bin/env bash
rm -rvf /javafxbinary/*
cp -vf /javafxlibrary-*-jar-with-dependencies.jar /javafxbinary/.
java -jar /javafxlibrary-*-jar-with-dependencies.jar

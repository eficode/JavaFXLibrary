#!/usr/bin/env bash
rm -rvf /javafxbinary/*
cp -vf /javafxlibrary-*-jar-with-dependencies.jar /javafxbinary/.
cp -vf /javafxlibrary-*-tests.jar /javafxbinary/.
chmod 555 /javafxbinary/*
java -jar /javafxlibrary-*-jar-with-dependencies.jar

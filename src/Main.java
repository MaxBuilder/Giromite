����   : 4  Main  java/lang/Object <init> ()V Code
  	   LineNumberTable LocalVariableTable this LMain; main ([Ljava/lang/String;)V  modele/plateau/Jeu
  	  vue_controleur/VueControleur
     (Lmodele/plateau/Jeu;)V
     getOrdonnanceur $()Lmodele/deplacements/Ordonnanceur;
     modele/deplacements/Ordonnanceur   ! addObserver (Ljava/util/Observer;)V
  # $ % 
setVisible (Z)V      ,
  ) * + start (J)V args [Ljava/lang/String; jeu Lmodele/plateau/Jeu; vc Lvue_controleur/VueControleur; 
SourceFile 	Main.java !               /     *� �    
                    	       x     &� Y� L� Y+� M+� ,� ,� "+ &� (�    
         	       %          & , -     . /    0 1   2    3
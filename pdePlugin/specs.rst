================================
 Nouvel algo pour le plugin PDE
================================

CLEAN
=====

- delete du ``$buildDirectory`` qui est dans le workspace hudson

- **si eclipse 3.3** :

    + delete du dossier ``links`` dans la target platform et recréation de celui ci mais vide
    + ajout du fichier link pour l'appli bidon **rcpcleaner** dans la TP
    + démarrage de la TP avec l'option ``-application com.thalesgroup.rcpcleaner.application -clean`` pour nettoyer les extensions de la TP

INIT
====

- création du dossier ``$buildDirectory`` dans le workspace hudson

PROCESS RESOURCES
=================

- en fonction de la liste des features donnée dans le script gradle, générer le fichier ``.map`` dans le ``$buildDirectory/maps``. le fichier ``.map`` devra contenir la liste des IDs des features ou plugins contenus dans les features ainsi que leur localisation physique sous le format suivant ::
        
    feature@id.de.la.feature=COPY,/répertoire/contenant/les/features,répertoire.de.la.feature
    plugin@id.du.plugin=COPY,/répertoire/contenant/les/plugins,répertoire.du.plugin

- copie du build.properties avec les options de fetch qui vont bien (``skipFetch`` et ``skipMaps`` commentés) dans ``$buildDirectory/builder``
- copie du ``customTargets.xml`` avec les targets de fetch qui vont bien ``$buildDirectory/builder`` ::

    <target name="checkLocalMaps">
        <available property="skipMaps" file="${buildDirectory}/maps" />
    </target>
    <target name="postFetch">
        <echo>Cleaning fetched sources...</echo>
        <delete includeemptydirs="true">
            <fileset dir="${buildDirectory}" includes="**/bin/**" />
        </delete>
        <echo>Cleaning fetched sources finished.</echo>
    </target>

- ajout de allElements.xml dans ``$buildDirectory/builder`` avec 1 entrée de ce type par feature ::

    <target name="allElementsDelegator">
        <ant antfile="${genericTargets}" target="${target}">
            <property name="type" value="feature" />
            <property name="id" value="feature.1.name" />
        </ant>
        <ant antfile="${genericTargets}" target="${target}">
            <property name="type" value="feature" />
            <property name="id" value="feature.2.name" />
        </ant>
        <ant antfile="${genericTargets}" target="${target}">
            <property name="type" value="feature" />
            <property name="id" value="feature.3.name" />
        </ant>
        ...
    </target>
    
    <target name="assemble.feature.1.name">
          <ant antfile="${assembleScriptName}" dir="${buildDirectory}"/>
    </target>
    <target name="assemble.feature.2.name">
          <ant antfile="${assembleScriptName}" dir="${buildDirectory}"/>
    </target>
    <target name="assemble.feature.3.name">
          <ant antfile="${assembleScriptName}" dir="${buildDirectory}"/>
    </target>
    ...

- **si build de product** : ajout dans la ligne de commande de l'option ``Dproduct=/dossier.du.product/machin.product`` (le slash part depuis ``$buildDirectory/plugins``)

- **si build de feature** : génération du ``allElements.xml`` avec toutes les features buildées (en fonction du contenu du fichier gradle) dans ``$buildDirectory/builder``

- **si eclipse 3.3 (usePreviousLinks = true)**

    + génération des fichiers .link depuis les dépendances dans le script gradle (à générer dans le dossier links de la TP) attention -> pas de ``/eclipse`` à la fin des extensions !!!
    + démarrage de la TP avec l'option ``-application com.thalesgroup.rcpcleaner.application -clean`` pour résoudre les extensions dans la TP.

- **si eclipse 3.5 (usePreviousLinks = false)**

    + génération de l'option pluginPath pour la passer en ligne de commande. Attention -> rajouter un ``/eclipse`` si il n'est pas présent à la fin des extensions !!!

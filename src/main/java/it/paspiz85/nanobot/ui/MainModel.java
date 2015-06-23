/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.Constants;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import aok.coc.launcher.BotLauncher;

/**
 *
 * @author v-ppizzuti
 */
public class MainModel implements Constants {

	private BotLauncher botLauncher = new BotLauncher();

	protected final Logger logger = Logger.getLogger(getClass().getName());
	private boolean setupDone = false;

	void botLauncherSetup() throws Exception {
		// TODO botLauncher.setup();
	}

	void botLauncherStart() throws Exception {
		botLauncher.start();
	}

	void botLauncherTearDown() {
		botLauncher.tearDown();
	}

	/**
	 * GitHub dependency is only used here and unused parts are excluded. Make
	 * sure it works fine if it is used somewhere else.
	 */
	public boolean checkForUpdate() {
		try {
			String current = getClass().getPackage().getImplementationVersion();
			if (current == null) {
				// IDE run
				return false;
			}
			DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(
					current);
			GitHub github = GitHub.connectAnonymously();
			GHRepository repository = github.getRepository(REPOSITORY);
			for (GHRelease r : repository.listReleases()) {
				String release = r.getName().substring(1);
				DefaultArtifactVersion releaseVersion = new DefaultArtifactVersion(
						release);
				if (currentVersion.compareTo(releaseVersion) < 0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unable to get latest version", e);
		}
		return false;
	}

	void initialize() {
	}

	public boolean isSetupDone() {
		return setupDone;
	}

	public void setSetupDone(boolean setupDone) {
		this.setupDone = setupDone;
	}

}

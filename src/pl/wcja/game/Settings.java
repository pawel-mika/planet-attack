package pl.wcja.game;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.wcja.game.ai.AILevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * 
 * @author <a href="mailto:ketonal80@gmail.com">Pablo</a>, wcja.pl
 *
 */
public class Settings {

	public static class HiScoreEntry implements Comparable<HiScoreEntry>{
		private Object gameMode = null;
		private Date date = Calendar.getInstance().getTime();
		private String name = "";
		private int score = 0;
		private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		
		public HiScoreEntry(String saveString) {
			parseSaveString(saveString);
		}
		
		public HiScoreEntry(String name, int score) {
			this.name = name;
			this.score = score;
		}

		public Date getDate() {
			return date;
		}

		public String getName() {
			return name;
		}

		public int getScore() {
			return score;
		}
		
		public Object getGameMode() {
			return gameMode;
		}

		@Override
		public int compareTo(HiScoreEntry o) {
			return getScore() - o.getScore();
		}
		
		public String getSaveString() {
			return String.format("%s;%s;%s;%s", getName().replaceAll(";", ","), getScore(), "", sdf.format(getDate()));
		}
		
		public void parseSaveString(String ss) {
			String[] parts = ss.split(";");
			name = parts[0];
			score = Integer.parseInt(parts[1]);
//			gameMode = parts[2];
			try {
				date = sdf.parse(parts[3]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}
	
	private Preferences preferences = null;
	
	private AILevel aiLevel = AILevel.NORMAL;
	private boolean soundEnabled = true;
	private float soundVolume = 0.75f;
	private boolean musicEnabled = false;
	private float musicVolume = 0.5f;
	private int maxHiScoreEntries = 10;
	private String defaultEmptyName = "Please enter your name";
	private String lastEnteredName = defaultEmptyName;
	private List<HiScoreEntry> hiScores = new ArrayList<Settings.HiScoreEntry>();
	
	public Settings() {
	}
	
	public List<HiScoreEntry> getHiScores() {
		return hiScores;
	}
	
	/**
	 * Check if score qualifies for hiscore (before asking user a name in gameresultscreen)
	 * @param score
	 * @return
	 */
	public boolean qualifiedForHiScore(int score) {
		if(hiScores.size() < maxHiScoreEntries) {
			return true;
		}
		if(score < Collections.min(hiScores).getScore()) {
			return false;
		}
		return true;
	}
	
	public void addHiScore(HiScoreEntry hse) {
		hiScores.add(hse);
		lastEnteredName = hse.getName();
		Collections.sort(hiScores, Collections.reverseOrder());
		if(hiScores.size() > maxHiScoreEntries) {
			hiScores.remove(maxHiScoreEntries);//³opatologicznie troche narazie...
		}
	}
	
	public boolean isDefaultEmptyName(String name) {
		return defaultEmptyName.equalsIgnoreCase(name);
	}
	
	public String getLastEnteredName() {
		return lastEnteredName;
	}

	public AILevel getAiLevel() {
		return aiLevel;
	}

	public void setAiLevel(AILevel aiLevel) {
		this.aiLevel = aiLevel;
	}
	
	public float getSoundVolume() {
		return soundVolume;
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}

	public void setSoundEnabled(boolean enabled) {
		this.soundEnabled = enabled;
	}
	
	public boolean isSoundEnabled() {
		return soundEnabled;
	}
	
	public void setMusicEnabled(boolean enabled) {
		this.musicEnabled = enabled;
	}
	
	public boolean isMusicEnabled() {
		return musicEnabled;
	}
	
	public void readSettings() {
		preferences = Gdx.app.getPreferences("planetattack");
		//if empty - init default
		if(preferences.get().size() == 0) {
			saveSettings();
		}
		aiLevel = AILevel.valueOf(preferences.getString("AI"));
		lastEnteredName = preferences.getString("lastEnteredName");
		Pattern p = Pattern.compile("hse\\d+");
		for(String s : preferences.get().keySet()) {
			Matcher m = p.matcher(s);
			if(m.matches()) {
				addHiScore(new HiScoreEntry(preferences.getString(s)));
			}
		}
	}
	
	public void saveSettings() {
		preferences.putString("AI", aiLevel.name());
		preferences.putString("lastEnteredName", lastEnteredName);
		int hseCounter = 0;
		for(HiScoreEntry hse : hiScores) {
			preferences.putString("hse" + hseCounter++ , hse.getSaveString());
		}
		preferences.flush();
	}
}

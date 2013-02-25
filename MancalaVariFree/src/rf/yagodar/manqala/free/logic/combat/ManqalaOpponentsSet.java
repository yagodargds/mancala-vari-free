package rf.yagodar.manqala.free.logic.combat;

import java.security.SecureRandom;

import rf.yagodar.manqala.free.logic.model.animated.ManqalaCharacter;

/**
 * Предназначен для хранения оппонентов игры Манкала.
 * 
 * @author Yagodar
 * @version 1.0.1, 16.10.2012
 */
public class ManqalaOpponentsSet {
	/**
	 * Создаёт экземпляр набора оппонентов игры Манкала. Оппоненты(их 2) не должны быть null.
	 * Оппонент не может играть против себя самого, то есть первый и второй оппонент различны.
	 * 
	 * @param firstOpponent первый оппонент
	 * @param secondOpponent второй оппонент
	 */
	public ManqalaOpponentsSet(ManqalaCharacter firstOpponent, ManqalaCharacter secondOpponent) {		
		if(firstOpponent == null || secondOpponent == null) {
			//TODO Ex
		}
		
		if(firstOpponent.equals(secondOpponent)) {
			//TODO Ex
		}
		
		this.firstOpponent = firstOpponent;
		this.secondOpponent = secondOpponent;		
	}
	
	public ManqalaCharacter getOpponent(Opponent opponentType) {
		switch(opponentType) {
		case RANDOM:
			if((new SecureRandom()).nextInt(101) > 50) {
				return firstOpponent;
			}
			else {
				return secondOpponent;
			}
		case FIRST:
			return firstOpponent;
		case SECOND:
			return secondOpponent;
		default:
			return null;
		}
	}

	/**
	 * Возвращает первого оппонента из набора.
	 * 
	 * @return первый оппонент.
	 */
	public ManqalaCharacter getFirstOpponent() {
		return(this.firstOpponent);
	}
	
	/**
	 * Возвращает второго оппонента из набора
	 * 
	 * @return второй оппонент.
	 */
	public ManqalaCharacter getSecondOpponent() {
		return(this.secondOpponent);
	}
	
	/**
	 * Проверяет, является ли <code>character</code> оппонентом. Если персонаж <code>null</code>, то
	 * вернёт <code>false</code>.
	 * 
	 * @param character персонаж на проверку
	 * @return <code>true</code> character является оппонентом из этого набора<br />
	 * 			<code>false</code> character не является оппонентом из этого набора
	 */
	public boolean isOpponent(ManqalaCharacter character) {
		if(character == null) {
			return(false);
		}
		
		return(character.equals(getFirstOpponent()) || character.equals(getSecondOpponent()));
	}
	
	public enum Opponent {
		RANDOM,
		FIRST,
		SECOND;
	}
	
	private ManqalaCharacter firstOpponent;
	private ManqalaCharacter secondOpponent;
}

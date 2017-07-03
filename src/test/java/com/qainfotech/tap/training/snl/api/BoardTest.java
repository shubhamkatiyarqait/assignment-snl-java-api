package com.qainfotech.tap.training.snl.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BoardTest {

	Board board;

	@BeforeMethod

	public void load() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
	GameInProgressException, MaxPlayersReachedExeption {
		board = new Board();
		board.registerPlayer("shubham");
		board.registerPlayer("sumit");


	}


	@Test(expectedExceptions=PlayerExistsException.class)
	public void acheckANameOfPlayerAlreadyExist() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		System.out.println(board);
		board.registerPlayer("sumit");
	}


	@Test(expectedExceptions=MaxPlayersReachedExeption.class)
	public void check_maximum_number_Of_Player() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		board.registerPlayer("shivam");
		board.registerPlayer("paras");
		board.registerPlayer("varun");
	}

	@Test(expectedExceptions=NoUserWithSuchUUIDException.class)
	public void No_User_With_Such_UUID() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
	GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {

		board.deletePlayer(UUID.randomUUID());

	}

	@Test
	public void Check_PlayerDeleted() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException,
	GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException, InvalidTurnException {

		UUID uuid= (UUID)board.getData().getJSONArray("players").getJSONObject(0).get("uuid");
		board.deletePlayer(uuid);

		assertThat(board.getData().length()).isEqualTo(3);
	}
	
	

	@Test(expectedExceptions=GameInProgressException.class)
	public void CheckIfGameAlreadyInProgress() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
	{
		
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(0)).get("uuid"));
		board.registerPlayer("Ayush");
	}


	@Test(expectedExceptions=InvalidTurnException.class)
	public void CheckInvalidTurn() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, InvalidTurnException, NoUserWithSuchUUIDException
	{
		board.rollDice((UUID) ((JSONObject) board.getData().getJSONArray("players").get(1)).get("uuid"));
		
	}
	
	



}

package com.ashtechsolution.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backGround;//Back ground image
	Texture bottomTube;
	Texture gameOver;
	int score = 0;
	int scoreingTube = 0;
	BitmapFont font;

	Circle birdCircle;//for collision detection
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
	//ShapeRenderer shapeRenderer;

	Texture topTube;
	float  velocity = 0;
	int gameStart = 0;//gameStart case 0-->initial,1-->start,2-->restart
	int gap = 400;
	float maxOffSet;
	Random randomGenerator;
	float[] tubeOffset = new float[4];
	float tubeVelocity = 4;
	float[] tubeX = new float[4];
	float numberOfTubes = 4;
	float distanceBetweenTubes;

	Texture[] birds;
	int flyingState = 0;
	float birdY = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		backGround = new Texture("bg.png");//image path
		birdCircle = new Circle();
		topTubeRectangle = new Rectangle[4];
		bottomTubeRectangle = new Rectangle[4];
		font = new BitmapFont();//score font properties
		font.setColor(Color.RED);
		font.getData().scale(10);
	//	shapeRenderer = new ShapeRenderer();//render the collision shapes
		gameOver = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

        startNew();


	}

	public void startNew(){

		birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;//bird position
		maxOffSet = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		//tubeX = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2;
		distanceBetweenTubes = Gdx.graphics.getWidth()*3/4;

		for(int i = 0;i<numberOfTubes;i++){

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() +i*distanceBetweenTubes;
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();


		}

	}

	@Override
	public void render () {
        //continues render start from here
		batch.begin();
		batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//back ground position

		if(gameStart == 1) {

			if(tubeX[scoreingTube] < Gdx.graphics.getWidth() * 1/4){
				score++;
				System.out.println("score" +score);
				if(scoreingTube < numberOfTubes - 1){
					scoreingTube++;
				}
				else{
					scoreingTube = 0;
				}
			}



			if (Gdx.input.justTouched()) {
				// touch input

			//	System.out.println("this is working fine");

				velocity = -20;

			}

			for(int i = 0;i<numberOfTubes;i++){

				if(tubeX[i] < -topTube.getWidth()){


					tubeX[i] +=numberOfTubes*distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}
				else{

					tubeX[i] = tubeX[i] - tubeVelocity;

				}



				batch.draw(topTube,tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube,tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 -bottomTube.getHeight() + tubeOffset[i]);
                topTubeRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
                bottomTubeRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 -bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
			}

            if(birdY > 0 && birdY < Gdx.graphics.getHeight() /*|| velocity < 0*/) {
				velocity++;
				birdY -= velocity;
			}
			else {
				gameStart = 2;
			}
		}
		else if (gameStart == 0){

			if(Gdx.input.justTouched()){

				gameStart = 1;

			}
		}
		else if (gameStart == 2){

			batch.draw(gameOver,Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight());
			if(Gdx.input.justTouched()){

				gameStart = 1;
				score = 0;
				scoreingTube = 0;
				velocity = 0;
				startNew();

			}

		}

		if (flyingState == 0) {
			flyingState = 1;
		} else {
			flyingState = 0;
		}

		batch.draw(birds[flyingState], Gdx.graphics.getWidth() / 2 - birds[flyingState].getWidth() / 2, birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flyingState].getHeight() / 2,birds[flyingState].getWidth() / 2);


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int i =0;i<numberOfTubes;i++){
        //	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
        //	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 -bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

        	if(Intersector.overlaps(birdCircle,topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i]) ){

			//	System.out.println("hai overlapped");
				gameStart = 2;


			}

        }
        //shapeRenderer.end();
		//System.out.println(score);

	}

	@Override
	public void dispose () {
		batch.dispose();
		backGround.dispose();
	}
}

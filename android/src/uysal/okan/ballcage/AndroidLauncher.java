package uysal.okan.ballcage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import uysal.okan.ballcage.util.GdxUtils;

public class AndroidLauncher extends AndroidApplication implements AdController, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener{

	private AdView bannerAdView;
	private InterstitialAd interstitialAd;

	private GoogleSignInClient signInClient;
	public GoogleApiClient client;

	@Override
	protected void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initAds();
		initUi();

	}

	private void initAds() {
		Games.GamesOptions gamesOptions;
		gamesOptions = Games.GamesOptions.builder().build();

		client = new GoogleApiClient.Builder( getApplicationContext() )
				.addApi( Games.API, gamesOptions )
				.build();

		bannerAdView = new AdView(this);
		bannerAdView.setId(R.id.adViewId);
		bannerAdView.setAdUnitId(AdUnitIds.BANNER_ID);
		bannerAdView.setAdSize(AdSize.SMART_BANNER);


		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AdUnitIds.INTERSTITIAL_ID);

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				loadInterstitial();
			}
		});

		loadInterstitial();
	}

	private void initUi() {
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new BallCage(this), config);

		RelativeLayout layout = new RelativeLayout(this);

		// ad view params
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
		);

		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		// game view params
		RelativeLayout.LayoutParams gameParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
		);

		//gameParams.addRule(RelativeLayout.ABOVE, bannerAdView.getId());


		layout.addView(gameView, gameParams);
		layout.addView(bannerAdView, adParams);

		setContentView(layout);
	}

	@Override
	public void connect() {
		signInSilently();
		/*if ( isConnected() ) { return; }

		client.registerConnectionCallbacks( this );
		client.registerConnectionFailedListener( this );
		client.connect();*/

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 9001) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				// The signed in account is stored in the result.
				GoogleSignInAccount signedInAccount = result.getSignInAccount();
			} else {

				String message = result.getStatus().getStatusMessage();
				if (message == null || message.isEmpty()) {
					message = "hata";
				}
				new AlertDialog.Builder(this).setMessage(message)
						.setNeutralButton(android.R.string.ok, null).show();
			}
		}
	}



	@Override
	public void unlockAchievement(int n) {
		if ( !isConnected() ) { return; }

		if ( n > AdUnitIds.ACHEIVEMENT.length ) { return; }
		else { Games.Achievements.unlock( client, AdUnitIds.ACHEIVEMENT[ n ] ); }
	}

	public boolean isConnected() {
		return client != null && client.isConnected();
	}

	@Override
	public void showLeaderboard() {
		/*if ( !isConnected() ) {
			connect();
			return; }
		Intent intent = Games.Leaderboards.getAllLeaderboardsIntent( client );
		this.startActivityForResult( intent, 9003 );*/
		try {
			GoogleSignInAccount signInClient2 = GoogleSignIn.getLastSignedInAccount(this);
			Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
					.getLeaderboardIntent(getString(R.string.leaderboard_high_score))
					.addOnSuccessListener(new OnSuccessListener<Intent>() {
						@Override
						public void onSuccess(Intent intent) {
							startActivityForResult(intent, 9004);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		bannerAdView.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		bannerAdView.pause();
	}

	@Override
	protected void onDestroy() {
		bannerAdView.destroy();
		super.onDestroy();
	}

	@Override
	public void showBanner() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loadBanner();
			}
		});
	}

	private void loadBanner() {
		if (isNetworkConnected()) {
			bannerAdView.loadAd(AdUtils.buildRequest());
			bannerAdView.setAdListener(new AdListener() {
				public void onAdLoaded() {
					GdxUtils.loadAd = true;
					bannerAdView.setVisibility(View.GONE);
					//bannerAdView.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	private void startSignInIntent() {
		signInClient = GoogleSignIn.getClient(this,
				GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
		Intent intent = signInClient.getSignInIntent();
		startActivityForResult(intent, 9001);
	}

	private void signInSilently() {
		signInClient = GoogleSignIn.getClient(this,
				GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
		signInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							// The signed in account is stored in the task's result.
							GoogleSignInAccount signedInAccount = task.getResult();
							System.out.println(signedInAccount.getEmail());
							System.out.println();
						} else {
							startSignInIntent();
							// Player will need to sign-in explicitly using via UI
							System.out.println();
						}
					}
				});
	}

	@Override
	public void showInterstitial() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showInterstitialInternal();
			}
		});
	}

	private void showInterstitialInternal() {
		if(interstitialAd.isLoaded()) {
			interstitialAd.show();
		}
	}

	private void loadInterstitial() {
		if(isNetworkConnected()) {
			interstitialAd.loadAd(AdUtils.buildRequest());
		}
	}

	@Override
	public boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	@Override
	public void hideandShowBanner() {
		/*bannerAdView.setVisibility(View.GONE);
		bannerAdView.setVisibility(View.VISIBLE);*/
		Thread thread = new Thread(){
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(1000);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								bannerAdView.setVisibility(View.GONE);
								bannerAdView.setVisibility(View.VISIBLE);
							}
						});

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		thread.start();
	}

	@Override
	public void hideBanner() {
		//bannerAdView.setVisibility(View.GONE);
		Thread thread = new Thread(){
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(1000);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								bannerAdView.setVisibility(View.GONE);
							}
						});

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		thread.start();
	}

	@Override
	public void ShowBanner() {
		/*try {
			bannerAdView.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println();
		}*/
		Thread thread = new Thread(){
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(1000);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								bannerAdView.setVisibility(View.VISIBLE);
							}
						});

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		thread.start();

	}

	public void someFunction() {

		new Thread(new Runnable() {
			public void run(){
				bannerAdView.setVisibility(View.VISIBLE);
			}
		}).start();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		int errorCode = connectionResult.getErrorCode();
		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, 9001);
		if (dialog != null) {
			dialog.show();
		}
		System.out.println();
	}
}

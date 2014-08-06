package com.dev_training.isc.hellouserauth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private static final String TAG = "PlaceholderFragment";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            rootView.findViewById(R.id.loginButton).setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.loginButton:
                    login();
                    break;
            }
        }

        private void login() {
            Log.d(TAG, "login begin");
            AccountManager accountManager = AccountManager.get(getActivity());
            Account[] accounts = accountManager.getAccounts();
            Account targetAccount = null;
            Handler handler = new Handler();
            final Bundle bundle = new Bundle();
            // gmailアカウントを探索
            for (int i=0; i<accounts.length; i++){
                if (accounts[i].name.indexOf("gmail.com")>-1) {
                    targetAccount = accounts[i];
                    break;
                }
            }
            accountManager.confirmCredentials(
                    targetAccount, // 最初のGoogleアカウントを選択
                    bundle, getActivity(), new AccountManagerCallback <Bundle>(){
                        @Override
                        public void run(AccountManagerFuture<Bundle> bundleAccountManagerFuture) {
                            //
                            Log.d(TAG, bundleAccountManagerFuture.toString());

                            try {
                                // 結果の取得
                                Bundle result = bundleAccountManagerFuture.getResult();
                                if (result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)){
                                    // Googleアカウントの
                                    // 再認証成功
                                    Toast.makeText(getActivity(), "認証に成功しました", Toast.LENGTH_LONG).show();
                                } else {
                                    // 再認証失敗
                                    Toast.makeText(getActivity(), "認証に失敗しました", Toast.LENGTH_LONG).show();
                                }

                            } catch (OperationCanceledException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (AuthenticatorException e) {
                                e.printStackTrace();
                            }

                        }
                    }, handler
            );

        }
    }
}

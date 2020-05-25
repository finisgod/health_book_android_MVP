package ru.mott;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import ru.mott.account.AccountSingleton;
import ru.mott.activities.ScanBarcodeActivity;
import ru.mott.architectureInterface.ArchitrectureInterface;
import ru.mott.data.CommonData;
import ru.mott.fragments.AddNewProductFragment;
import ru.mott.fragments.BarcodeNotFoundFragment;
import ru.mott.fragments.ChooseFragment;
import ru.mott.fragments.DownloadFragment;
import ru.mott.fragments.EmailAndPasswordFragment;
import ru.mott.fragments.MainMenuFragment;
import ru.mott.fragments.OptionFragment;
import ru.mott.fragments.ProductFragment;
import ru.mott.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements ArchitrectureInterface.View {
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainActivityPresenter((AccountSingleton) this.getApplication());

        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment first = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (first == null) {
                transaction.add(R.id.fragment_container, new EmailAndPasswordFragment());
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void onItemSelected(int call) {
        change(call);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if ("No".equals(data.getStringExtra("barcode"))) {
            Toast.makeText(getApplicationContext(), "Barcode not found", Toast.LENGTH_SHORT).show();

            change(CommonData.barcodeNotFound);
        } else {
            String barcode = presenter.getBarcode();
            Toast.makeText(getApplicationContext(), barcode, Toast.LENGTH_SHORT).show();
            change(CommonData.download);
        }
    }

    private void change(int call) {
        switch (call) {
            case CommonData.barcodeScan:
                Intent intent = new Intent(this, ScanBarcodeActivity.class);
                startActivityForResult(intent, 1);
                break;
            case CommonData.mainMenu:
                MainMenuFragment fragment2 = new MainMenuFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment2)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment2)
                        .commit();
                break;
            case CommonData.options:
                OptionFragment fragment3 = new OptionFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment3)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment3)
                        .commit();
                break;
            case CommonData.chooseFrag:
                ChooseFragment fragment4 = new ChooseFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment4)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment4)
                        .commit();
                break;
            case CommonData.emailAndPass:
                EmailAndPasswordFragment fragment5 = new EmailAndPasswordFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment5)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment5)
                        .commit();
                break;
            case CommonData.productFragment:
                ProductFragment fragment7 = new ProductFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment7)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment7)
                        .commit();
                break;
            case CommonData.barcodeNotFound:
                BarcodeNotFoundFragment fragment8 = new BarcodeNotFoundFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment8)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment8)
                        .commit();
                break;
            case CommonData.addNew:
                AddNewProductFragment fragment9 = new AddNewProductFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment9)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment9)
                        .commit();
                break;
            case CommonData.download:
                DownloadFragment fragment6 = new DownloadFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment6)
                        .addToBackStack(null)
                        .setPrimaryNavigationFragment(fragment6)
                        .commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!presenter.isUserLog()) {
            moveTaskToBack(false);
        } else if (Objects.requireNonNull(getSupportFragmentManager().getPrimaryNavigationFragment()).getClass().isInstance(new MainMenuFragment())) {
            moveTaskToBack(false);
        } else if (Objects.requireNonNull(getSupportFragmentManager().
                getPrimaryNavigationFragment()).getClass().isInstance(new ChooseFragment())
                || Objects.requireNonNull(getSupportFragmentManager().
                getPrimaryNavigationFragment()).getClass().isInstance(new ProductFragment())
                || Objects.requireNonNull(getSupportFragmentManager().
                getPrimaryNavigationFragment()).getClass().isInstance(new AddNewProductFragment())) {
            change(CommonData.mainMenu);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}

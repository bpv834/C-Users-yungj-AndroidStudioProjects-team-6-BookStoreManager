package com.lion.bookstore_manager.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.bookstore_manager.MainActivity
import com.lion.bookstore_manager.databinding.FragmentLocationBinding
import com.skt.tmap.TMapView


class LocationFragment : Fragment() {
    // 권한 확인을 위한 런처
    lateinit var permissionCheckLauncher: ActivityResultLauncher<Array<String>>
    // 위치 정보 관리 객체
    lateinit var locationManager: LocationManager
    // 위치 측정을 하면 반응하는 리스너
    lateinit var myLocationListener: MyLocationListener

    // 사용자의 현재 위치를 담을 변수
    var userLocation:Location? = null

    lateinit var mainActivity: MainActivity

    lateinit var fragmentLocationBinding: FragmentLocationBinding

    // 확인할 권한 목록
    val permissionList = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentLocationBinding = FragmentLocationBinding.inflate(inflater)

        // LocationManager와 리스너 초기화
        locationManager = mainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocationListener = MyLocationListener() // 초기화

        settingMap()


        // 권한 확인을 위한 런처 생성 메서드 호출
        createPermissionCheckLauncher()
        // 위치 가져오기
        getMyLocation()
        return fragmentLocationBinding.root
    }

    fun settingMap() {
        val tMapView = TMapView(mainActivity)
        fragmentLocationBinding.apply {
            tmapViewContainer.addView(tMapView)
            tMapView.setSKTMapApiKey("T0TJS7WCG89ZTJCXvlPNh8UusIkjS544aZ94ZOVD")
            tMapView.setOnMapReadyListener {
                tMapView.setCenterPoint(127.027610, 37.497942) // 서울 강남 중심 좌표 예시
            }
        }
    }


    // 애플리케이션의 설정화면을 실행시키는 메서드
    fun startSettingActivity(){
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("권한 확인 요청")
        materialAlertDialogBuilder.setMessage("권한을 모두 허용해줘야 정상적은 서비스 이용이 가능합니다")
        materialAlertDialogBuilder.setPositiveButton("권한 설정 하기"){ dialogInterface: DialogInterface, i: Int ->
            val uri = Uri.fromParts("package", mainActivity.packageName, null)
            val permissionIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(permissionIntent)
        }
        materialAlertDialogBuilder.show()
    }


    // 권한 확인을 위해 사용할 런처 생성
    fun createPermissionCheckLauncher(){
        // 런처를 등록한다.
        permissionCheckLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            // Snackbar.make(activityMainBinding.root, "권한 확인 완료", Snackbar.LENGTH_LONG).show()
            // 모든 권한에 대해 확인한다.
            permissionList.forEach { permissionName ->
                // 현재 권한이 허용되어 있지 않다면 다이얼로그를 띄운다.
                if(it[permissionName] == false){
                    // 설정 화면을 띄우는 메서드를 호출한다.
                    startSettingActivity()
                    // 함수 종료
                    return@registerForActivityResult
                }
            }
        }
    }

    // 현재 위치를 측정하는 메서드
    fun getMyLocation(){
        Log.d("test100","getMyLocation()")
        // 권한 허용 여부
        val check1 = ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val check2 = ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        // 만약 거부한 것이 하나라도 있다면
        if(check1 == PackageManager.PERMISSION_DENIED || check2 == PackageManager.PERMISSION_DENIED){
            // 권한 확인을 위한 Activity를 실행한다.
            permissionCheckLauncher.launch(permissionList)
            return
        }

        // GPS 프로바이더 사용이 가능하다면
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, myLocationListener)
        }
        // Network 프로바이더 사용이 가능하다면
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.0f, myLocationListener)
        }
    }

    // 위치 측정에 성공하면 동작하는 리스너
    inner class MyLocationListener : LocationListener {
        // 새로운 위치가 측정되면 호출되는 메서드
        // location : 새롭게 측정된 위도와 경도값이 담긴 객체
        override fun onLocationChanged(location: Location) {
            Log.d("test100", "Location changed: Lat=${location.latitude}, Lng=${location.longitude}")

            // 측정된 새로운 위치를 전달한다.
            setMyLocation(location)
        }
    }

    // 지도에 현재 위치를 표시하는 메서드
    fun setMyLocation(location: Location){
        // 로그로 현재 위치 출력
        Log.d("test100", "Current Location: Lat=${location.latitude}, Lng=${location.longitude}")

        // 사용자의 현재 위치를 변수에 담아준다.
        userLocation = location

        // 위치 측정 중지
        locationManager.removeUpdates(myLocationListener)

    }


}
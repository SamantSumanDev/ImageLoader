package com.samant.acharyaassignment.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samant.acharyaassignment.api.ApiService
import com.samant.acharyaassignment.repository.MainRepository
import com.samant.acharyaassignment.ui.Model.Coverage
import com.samant.acharyaassignment.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel : ViewModel() {


    private val _media_coverage = MutableLiveData<ApiState<List<Coverage>>>()
    val MediaCoverageLiveData: LiveData<ApiState<List<Coverage>>> = _media_coverage

    fun getMediaCoverage(
        limit: Int,
        api: ApiService
    ) {
        _media_coverage.value = ApiState.loading()

        viewModelFactory {

            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        val res = MainRepository.getMediaCoverages(
                            limit,
                            api
                        )
                            .execute()
                            .body()
                        if (res?.toString()?.isNotEmpty() == true) {
                            Log.d("tagM", "area: $res")
                            if (res!=null){
                                _media_coverage.postValue(ApiState.success(res))
                            }
                        } else {
                            _media_coverage.postValue(
                                ApiState.error(
                                    res?.toString() ?: "Something went wrong"
                                )
                            )
                        }
                    }
                } catch (e: HttpException) {
                    Log.d("HttpException", "exp: ${e.code()} ${e.response()}")
                    _media_coverage.postValue(ApiState.error("HTTP ${e.code()} error"))
                } catch (e: Exception) {
                    Log.d("tag: Exception", "exp: $e")
                    _media_coverage.postValue(
                        ApiState.error(
                            e.localizedMessage ?: "exception: Something went wrong: $e"
                        )
                    )
                }
            }
        }

    }

}
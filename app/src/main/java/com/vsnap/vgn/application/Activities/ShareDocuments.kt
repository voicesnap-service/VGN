package com.vsnap.vgn.application.Activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Adapters.ShareDocumentAdapter
import com.vsnap.vgn.application.Interface.ShareDocumentCheckListener
import com.vsnap.vgn.application.Modal.GetShareDocumentData
import com.vsnap.vgn.application.Modal.TemplateList
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard


class ShareDocuments : AppCompatActivity() {
    var dashboardViewmodel: Dashboard? = null
    var sharedocumentadapter: ShareDocumentAdapter? = null
    var ProjectID: Int? = null
    var CustomerID: Int? = null
    var Conference_ID: Int? = null
    var ShareDocumentData: List<GetShareDocumentData> = ArrayList()
    var TempLateList: List<TemplateList> = ArrayList()

    var documentidList: ArrayList<String> = ArrayList()

    @JvmField
    @BindView(R.id.recyclerDocument)
    var recyclerDocument: RecyclerView? = null

    @JvmField
    @BindView(R.id.lblNoRecordsFound)
    var lblNoRecordsFound: TextView? = null

    @JvmField
    @BindView(R.id.txtDescription)
    var txtDescription: EditText? = null

    @JvmField
    @BindView(R.id.btnShareDocument)
    var btnShare: Button? = null

    @JvmField
    @BindView(R.id.spinner)
    var spinners: Spinner? = null

    var Template_ID : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_documents)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.actionbar_layout)
        val view = supportActionBar!!.customView
        val lblTitle = view.findViewById<View>(R.id.lblTitle) as TextView
        val imgBack = view.findViewById<View>(R.id.imgBack) as ImageView
        val rytAction = view.findViewById<View>(R.id.rytAction) as RelativeLayout
        ButterKnife.bind(this)

        ProjectID = intent.getIntExtra("Projectid", 0)
        CustomerID = intent.getIntExtra("CustomerID", 0)
        Conference_ID = intent.getIntExtra("ConfID", 0)

        Log.d("ProjectID", ProjectID.toString())
        Log.d("CustomerID", CustomerID.toString())
        Log.d("Conference_ID", Conference_ID.toString())

        lblTitle.text = "Share Docs"
        imgBack.setOnClickListener({
            onBackPressed()
        })

        btnShare!!.setOnClickListener({
            if (documentidList.size > 0) {
                shareDocument()

            } else {
                CommonUtil.ApiAlert(this, "Kindly Select any one document")
            }
        })


        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()

        var token = SharedPreference.getSHToken(this)
        dashboardViewmodel!!.getShareDocumentList(ProjectID!!, token!!, this)

        dashboardViewmodel!!.GetShareDocumentMutableLiveData!!.observe(this) { response ->
            if (response != null) {
                Log.d("ShareDocsRsponse", "test")
                val status = response.status
                val message = response.message
                if (status == 1) {
                    ShareDocumentData = response.data!!
                    if (ShareDocumentData.size > 0) {
                        lblNoRecordsFound!!.visibility = View.GONE
                        recyclerDocument!!.visibility = View.VISIBLE
                        sharedocumentadapter = ShareDocumentAdapter(ShareDocumentData, this,
                            object : ShareDocumentCheckListener {
                                override fun add(data: GetShareDocumentData?) {
                                    var documentid = data!!.document_id
                                    documentidList.add(documentid.toString())
                                }

                                override fun remove(data: GetShareDocumentData?) {
                                    var documentid = data!!.document_id
                                    documentidList.remove(documentid.toString())
                                }
                            })
                        val mLayoutManager: RecyclerView.LayoutManager =
                            LinearLayoutManager(this)
                        recyclerDocument!!.layoutManager = mLayoutManager
                        recyclerDocument!!.itemAnimator = DefaultItemAnimator()
                        recyclerDocument!!.adapter = sharedocumentadapter
                        recyclerDocument!!.recycledViewPool.setMaxRecycledViews(0, 80)
                        sharedocumentadapter!!.notifyDataSetChanged()
                    } else {
                        lblNoRecordsFound!!.visibility = View.VISIBLE
                        recyclerDocument!!.visibility = View.GONE

                    }
                } else {
                    lblNoRecordsFound!!.visibility = View.VISIBLE
                    recyclerDocument!!.visibility = View.GONE
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                lblNoRecordsFound!!.visibility = View.VISIBLE
                recyclerDocument!!.visibility = View.GONE

            }
        }

        dashboardViewmodel!!.SubmitShareDocumentLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    CommonUtil.AlertFinishOk(this, message)
                } else {
                    CommonUtil.ApiAlert(this, message)
                }

            } else {
                CommonUtil.ApiAlert(this, "Something went wrong")
            }
        }



        dashboardViewmodel!!.getTemplate(this,token!!)
        dashboardViewmodel!!.TemplatesLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    TempLateList = response.data!!

                    val templates = arrayOfNulls<String>(TempLateList.size);
                    for (data in TempLateList) {
                        templates[TempLateList.indexOf(data)] = data.template_id
                    }

                    if (spinners != null) {
                        val adapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item, templates
                        )
                        spinners!!.adapter = adapter

                        spinners!!.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {

                                Template_ID = TempLateList.get(position).id!!
                                val template:String? = TempLateList.get(position).template_text!!
                                Log.d("template_text",template!!)
                                txtDescription!!.text = Editable.Factory.getInstance().newEditable(template)

                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }
                        Log.d("TemplateSize", TempLateList.size.toString())
                    }


                } else {
                    CommonUtil.ApiAlert(this, "Something went wrong")
                }
            }


        }



    }

    private fun shareDocument() {
        var token = SharedPreference.getSHToken(this)
        var description = txtDescription!!.text.toString()
        Log.d("OpentTextdescription", description)
        val jsonObject = JsonObject()
        jsonObject.addProperty(ApiRequestNames.Req_AgentID, CommonUtil.UserData!!.member_id)
        jsonObject.addProperty(ApiRequestNames.Req_CustomerID, CustomerID)
        jsonObject.addProperty(ApiRequestNames.Req_TemplateId, Template_ID)
        jsonObject.addProperty(ApiRequestNames.Req_conference_id, Conference_ID)

        val jsonArray1 = Gson().toJsonTree(documentidList).asJsonArray

        jsonObject.add(ApiRequestNames.Req_documentid, jsonArray1)
        jsonObject.addProperty(ApiRequestNames.Req_opentext, description)
        dashboardViewmodel!!.SubmitShareDocumentWhatApp(jsonObject, token!!, this)

        Log.d("jsonObject", jsonObject.toString())
    }
}
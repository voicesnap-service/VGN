package com.vsnap.vgn.application.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Spannable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vsnap.vgn.application.Adapters.ShareDocumentAdapter
import com.vsnap.vgn.application.Interface.ShareDocumentCheckListener
import com.vsnap.vgn.application.Modal.GetShareDocumentData
import com.vsnap.vgn.application.Modal.TempType
import com.vsnap.vgn.application.Modal.TemplateList
import com.vsnap.vgn.application.R
import com.vsnap.vgn.application.Respository.ApiRequestNames
import com.vsnap.vgn.application.Utils.CommonUtil
import com.vsnap.vgn.application.Utils.SharedPreference
import com.vsnap.vgn.application.ViewModel.Dashboard
import java.util.*
import kotlin.collections.ArrayList


class ShareDocuments : AppCompatActivity() {
    var dashboardViewmodel: Dashboard? = null
    var sharedocumentadapter: ShareDocumentAdapter? = null
    var ProjectID: Int? = null
    var CustomerID: Int? = null
    var Conference_ID: Int? = null
    var ShareDocumentData: List<GetShareDocumentData> = ArrayList()
    var TempLateList: List<TemplateList> = ArrayList()
    var TempWhatsappTypeList: List<TempType> = ArrayList()

    var indextStringArry: ArrayList<String> = ArrayList()
    var edittedValues: ArrayList<String> = ArrayList()

    var documentidList: ArrayList<String> = ArrayList()
    var wordtoSpan: Spannable? = null

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
    @BindView(R.id.lnrDynamicLayout)
    var lnrDynamicLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.layoutDescription)
    var layoutDescription: ConstraintLayout? = null

    @JvmField
    @BindView(R.id.spinner)
    var spinners: Spinner? = null

    @JvmField
    @BindView(R.id.rytSpinner2)
    var rytSpinner2: RelativeLayout? = null


    @JvmField
    @BindView(R.id.spinnerWhatsAppTypes)
    var spinnerWhatsAppTypes: Spinner? = null


    var Template_ID : Int? = null
    var Template_Type : String? = ""

    var lnrParent : LinearLayout? = null
    var multiplesOfFour : Int? = 4
    var txtEditableArray: ArrayList<EditText> = ArrayList()
    var text_array_list: ArrayList<String> = ArrayList()



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
            text_array_list.clear()
            for ((index,value) in txtEditableArray.withIndex()){
                if(!value.text.toString().equals("#var#")){
                    text_array_list.add(value.text.toString())
                }
                else{
                    text_array_list.add("")
                }
            }
            Log.d("text_array", Gson().toJsonTree(text_array_list).asJsonArray.toString())
            if (documentidList.size > 0) {
                shareDocument()

            } else {
                CommonUtil.ApiAlert(this, "Kindly Select any one document")
            }
        })


        dashboardViewmodel = ViewModelProvider(this).get(Dashboard::class.java)
        dashboardViewmodel!!.init()

        var token = SharedPreference.getSHToken(this)
//        dashboardViewmodel!!.getShareDocumentList(ProjectID!!, token!!, this)

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
                    }
                    else {
                        lblNoRecordsFound!!.visibility = View.GONE
                        recyclerDocument!!.visibility = View.GONE
                    }
                } else {
                    lblNoRecordsFound!!.visibility = View.GONE
                    recyclerDocument!!.visibility = View.GONE
                    CommonUtil.ApiAlert(this, message)
                }
            } else {
                lblNoRecordsFound!!.visibility = View.GONE
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

        dashboardViewmodel!!.getTemplateTypes(this,token!!)
        dashboardViewmodel!!.WhatsAppTemplateTypesLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    TempWhatsappTypeList = response.data!!
                    val templates = arrayOfNulls<String>(TempWhatsappTypeList.size+1);
                    templates[0] = "Select Type"
                    for (data in TempWhatsappTypeList) {
                        templates[TempWhatsappTypeList.indexOf(data)+1] = data.unnest
                    }
                    if (spinnerWhatsAppTypes != null) {
                        val adapter = ArrayAdapter(
                            this,
                            R.layout.text_spinner,R.id.text1, templates
                        )
                        spinnerWhatsAppTypes!!.adapter = adapter

                        spinnerWhatsAppTypes!!.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {

                                if(position != 0) {
                                    Template_Type = TempWhatsappTypeList.get(position-1).unnest!!
                                    dashboardViewmodel!!.getTemplate(
                                        this@ShareDocuments,
                                        token!!,
                                        Template_Type!!
                                    )
                                }

                            }
                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }
                        Log.d("TemplateTypeSize", TempWhatsappTypeList.size.toString())
                    }


                } else {
                    CommonUtil.ApiAlert(this, "Something went wrong")
                }
            }
        }

        dashboardViewmodel!!.TemplatesLiveData!!.observe(this) { response ->
            if (response != null) {
                val status = response.status
                val message = response.message
                if (status == 1) {
                    TempLateList = response.data!!
                    rytSpinner2!!.visibility = View.VISIBLE
                    val templates = arrayOfNulls<String>(TempLateList.size+1);
                    templates[0] = "Select Template"

                    for (data in TempLateList) {
                        templates[TempLateList.indexOf(data)+1] = data.template_id
                    }
                    if (spinners != null) {
                        val adapter = ArrayAdapter(
                            this,
                            R.layout.text_spinner,R.id.text1, templates
                        )
                        spinners!!.adapter = adapter

                        spinners!!.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            @SuppressLint("ResourceAsColor")
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {

                                if(position !=0 ){

                                Template_ID = TempLateList.get(position-1).id!!
                                val template: String? = TempLateList.get(position-1).template_text!!
                                val split_arr = template!!.split(" ")
                                Log.d("template_length", split_arr.size.toString());
                                layoutDescription!!.visibility = View.VISIBLE

                                Log.d("SelectedValue", template)

                                if (template.contains("#var#")) {

                                    txtDescription!!.visibility = View.GONE
                                    lnrDynamicLayout!!.visibility = View.VISIBLE

                                    lnrDynamicLayout!!.removeAllViews()
                                    multiplesOfFour = 4
                                    txtEditableArray.clear()

                                    val ll = LinearLayout(this@ShareDocuments)
                                    ll!!.orientation = LinearLayout.HORIZONTAL

                                    val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    ll!!.layoutParams = layoutParams

                                    lnrParent = ll
                                    lnrDynamicLayout!!.addView(lnrParent)
                                    indextStringArry!!.clear()
                                    for ((index, data) in split_arr.withIndex()) {

                                        if (index == multiplesOfFour) {
                                            multiplesOfFour = multiplesOfFour!! + 4
                                            val ll = LinearLayout(this@ShareDocuments)
                                            ll!!.orientation = LinearLayout.HORIZONTAL
                                            val layoutParams = LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                            ll!!.layoutParams = layoutParams
                                            lnrParent = ll
                                            lnrDynamicLayout!!.addView(lnrParent)
                                        }

                                        if (data.contains("#var#")) {
                                            indextStringArry!!.add(index.toString())
                                            val ed = EditText(this@ShareDocuments)
                                            val lparams =
                                                LinearLayout.LayoutParams(0, 120) // Width , height
                                            lparams.weight = 2.0f;
                                            lparams.setMargins(5, 5, 5, 5);
                                            ed.setBackground(
                                                this@ShareDocuments.getResources()
                                                    .getDrawable(R.drawable.bg_rectangle_edittextbox)
                                            )
                                            ed.maxLines = 1
                                            ed.setGravity(Gravity.LEFT)
                                            ed.setTextSize(12F)
                                            ed.isEnabled = true
                                            ed.setTextColor(R.color.vgn_color)
                                            ed.layoutParams = lparams
                                            lnrParent!!.addView(ed)
                                            txtEditableArray.add(ed)


                                        } else {
                                            val ed = EditText(this@ShareDocuments)
                                            ed.setGravity(Gravity.CENTER_HORIZONTAL)
                                            val lparams =
                                                LinearLayout.LayoutParams(
                                                    0,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                                )
                                            lparams.weight = 1.0f;
                                            lparams.setMargins(10, 0, 0, 0);
                                            ed.setBackground(
                                                this@ShareDocuments.getResources()
                                                    .getDrawable(R.drawable.bg_white)
                                            )
                                            ed.isEnabled = false
                                            ed.setText(data + " ")
                                            ed.setTextSize(12F)
                                            ed.setTextColor(R.color.vgn_color)
                                            ed.layoutParams = lparams
                                            lnrParent!!.addView(ed)

                                        }
                                    }
                                } else {
                                    txtDescription!!.visibility = View.VISIBLE
                                    lnrDynamicLayout!!.visibility = View.GONE
                                    txtDescription!!.setText(template)
                                    txtDescription!!.isEnabled = false

                                }


                                var token = SharedPreference.getSHToken(this@ShareDocuments)
                                dashboardViewmodel!!.getShareDocumentList(
                                    ProjectID!!,
                                    token!!,
                                    this@ShareDocuments
                                )
                            }

                                else{
                                    layoutDescription!!.visibility = View.GONE
                                }


                        }
                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }
                        Log.d("TemplateSize", TempLateList.size.toString())
                    }
                }

                else {
                    rytSpinner2!!.visibility = View.GONE
                    layoutDescription!!.visibility = View.GONE
                    spinners!!.adapter = null
                    txtDescription!!.setText("")
                    lblNoRecordsFound!!.visibility = View.GONE
                    recyclerDocument!!.visibility = View.GONE
                    CommonUtil.ApiAlert(this, message)
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
        jsonObject.add(ApiRequestNames.Req_text_array, Gson().toJsonTree(text_array_list).asJsonArray)

        val jsonArray1 = Gson().toJsonTree(documentidList).asJsonArray

        jsonObject.add(ApiRequestNames.Req_documentid, jsonArray1)
        jsonObject.addProperty(ApiRequestNames.Req_opentext, description)
        dashboardViewmodel!!.SubmitShareDocumentWhatApp(jsonObject, token!!, this)

        Log.d("jsonObject", jsonObject.toString())
    }
}
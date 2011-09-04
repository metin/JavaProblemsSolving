<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Milestone>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Edit
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <script type="text/javascript">
        $(function () {
            $("#MilestonePlannedDate").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true, dateFormat: 'mm/dd/yy' });
            $("#MilestonePlannedDate").datepicker("option", "dateFormat", 'mm/dd/yy');
            $("#MilestoneDeliveryDate").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true, dateFormat: 'mm/dd/yy' });
            $("#MilestoneDeliveryDate").datepicker("option", "dateFormat", 'mm/dd/yy');
        });
    </script>

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>


    <div class="grid_10">
        <div class="section"> 
            <h1>Edit Milestone</h1>
            <p>Add more details or finish milestone.</p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                <%: Html.HiddenFor(model => model.MilestoneID) %>
                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.ProjID) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.ProjID, ViewBag.projects as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.ProjID)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.MilestonePlannedDate)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.MilestonePlannedDate)%>
                            <%: Html.ValidationMessageFor(model => model.MilestonePlannedDate)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.MilestoneDeliverable)%></th>
                        <td>
                            <%: Html.TextAreaFor(model => model.MilestoneDeliverable)%>
                            <%: Html.ValidationMessageFor(model => model.MilestoneDeliverable)%>
                        </td>
                    </tr>

                    <tr>
                        <th> <%: Html.LabelFor(model => model.ToBeDelivered)%></th>
                        <td>
                            <%: Html.TextAreaFor(model => model.ToBeDelivered)%>
                            <%: Html.ValidationMessageFor(model => model.ToBeDelivered)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.MilestoneDeliveryDate)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.MilestoneDeliveryDate)%>
                            <%: Html.ValidationMessageFor(model => model.MilestoneDeliveryDate)%>
                        </td>
                    </tr>

                </table>
                <div class="clear"></div> 
                <div>
                    <input type="submit" value="Update" class="action_button"/>
                </div>
                <div class="clear"></div>
            <% } %>
        </div>
    </div>
    <div class="clear"></div> 



</asp:Content>

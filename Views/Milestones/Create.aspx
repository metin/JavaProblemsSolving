<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Milestone>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Create
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <script type="text/javascript">
        $(function () {
            $("#MilestonePlannedDate").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true });
        });
	</script>

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section"> 
            <h1>New Milestone</h1>
            <p>Define milestone by project</p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
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
                </table>
                <div class="clear"></div> 
                <div>
                    <input type="submit" value="Create" class="action_button"/>
                </div>
                <div class="clear"></div>
            <% } %>
        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>

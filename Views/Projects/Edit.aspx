<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Pm.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Project>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Edit
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <script type="text/javascript">
        $(function () {
            $("#StartDate").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true, dateFormat: 'mm/dd/yy' });
            $("#StartDate").datepicker("option", "dateFormat", 'mm/dd/yy');

            $("#EndDate").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true, dateFormat: 'mm/dd/yy' });
            $("#EndDate").datepicker("option", "dateFormat", 'mm/dd/yy');

        });
	</script>

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>


    <div class="grid_10">
        <div class="section">
            <h1>Edit Project</h1>
            <p>Edit project <%: Model.ProjName %></p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>

                <%: Html.HiddenFor(model => model.ProjID) %>

                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.ProjName) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.ProjName)%>
                            <%: Html.ValidationMessageFor(model => model.ProjName)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.ProjBudget)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.ProjBudget)%>
                            <%: Html.ValidationMessageFor(model => model.ProjBudget)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.StartDate)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.StartDate)%>
                            <%: Html.ValidationMessageFor(model => model.StartDate)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.ProjManager) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.ProjManager, ViewBag.employees as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.ProjManager)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.ProjDept) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.ProjDept, ViewBag.departments as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.ProjDept)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.EndDate)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.EndDate)%>
                            <%: Html.ValidationMessageFor(model => model.EndDate)%>
                        </td>
                    </tr>
                </table>
                <div class="clear"></div> 
                <div>
                    <input type="submit" value="Save" class="action_button"/>
                </div>
                <div class="clear"></div>

            <% } %>
        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>


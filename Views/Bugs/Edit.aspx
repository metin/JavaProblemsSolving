<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Bug>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Edit
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">


    <script type="text/javascript">
        $(function () {
            $("#DateReported").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true, dateFormat: 'mm/dd/yy' });
            $("#DateReported").datepicker("option", "dateFormat", 'mm/dd/yy');
            $("#DateClosed").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true, dateFormat: 'mm/dd/yy' });
            $("#DateClosed").datepicker("option", "dateFormat", 'mm/dd/yy');
        });
    </script>

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>


    <div class="grid_10">
        <div class="section"> 
            <h1>Edit Bug</h1>
            <p>Add more details or close bug.</p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                <%: Html.HiddenFor(model => model.BugID) %>
                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.ProjID) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.ProjID, ViewBag.projects as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.ProjID)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.DateReported)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.DateReported)%>
                            <%: Html.ValidationMessageFor(model => model.DateReported)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.Details)%></th>
                        <td>
                            <%: Html.TextAreaFor(model => model.Details)%>
                            <%: Html.ValidationMessageFor(model => model.Details)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.EmpID)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.EmpID, ViewBag.employees as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.EmpID)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.Type ) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.Type, ViewBag.types as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.Type)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.DateClosed)%></th>
                        <td>
                            <%: Html.EditorFor(model => model.DateClosed)%>
                            <%: Html.ValidationMessageFor(model => model.DateClosed)%>
                        </td>
                    </tr>
                    <tr>
                        <th><%: Html.LabelFor(model => model.Status ) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.Status, ViewBag.statees as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.Status)%>
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
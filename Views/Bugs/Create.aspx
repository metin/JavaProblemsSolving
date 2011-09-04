<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Bug>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Create
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">


    <script type="text/javascript">
        $(function () {
            $("#DateReported").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true });
        });
	</script>

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section"> 
            <h1>Report a Bug</h1>
            <p>Enter details of the bug</p>
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
                        <th><%: Html.LabelFor(model => model.Type ) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.Type, ViewBag.types as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.Type)%>
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

<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Office>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Edit
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

       
    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section"> 
            <h1>Edit Office</h1>
            <p>Change office details</p>
            <br />
            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                 <%: Html.HiddenFor(model => model.OfficeID) %>
                <table class="details">
                    <tr>
                        <th><%: Html.LabelFor(model => model.OfficeNumber) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.OfficeNumber)%>
                            <%: Html.ValidationMessageFor(model => model.OfficeNumber)%>
                        </td>
                    </tr>
                    <tr>
                        <th> <%: Html.LabelFor(model => model.BuildingID)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.BuildingID, ViewBag.buildings as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.BuildingID)%>
                        </td>
                    </tr>

                    <tr>
                        <th> <%: Html.LabelFor(model => model.DeptID) %></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.DeptID, ViewBag.departments as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.DeptID) %>
                        </td>
                    </tr>

                    <tr>
                        <th> <%: Html.LabelFor(model => model.Area) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.Area)%>
                            <%: Html.ValidationMessageFor(model => model.Area) %>
                        </td>
                    </tr>

                    <tr>
                        <th> <%: Html.LabelFor(model => model.RoomType)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.RoomType, ViewBag.types as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.RoomType)%>
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


<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Division>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Create
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section"> 
            <h1>New Division</h1>
            <p>Create new division</p>
            <br />

            <% using (Html.BeginForm()) { %>
                <%: Html.ValidationSummary(true) %>
                <table class="details">
                    <tr>
                        <th> <%: Html.LabelFor(model => model.DivName) %></th>
                        <td>
                            <%: Html.EditorFor(model => model.DivName)%>
                            <%: Html.ValidationMessageFor(model => model.DivName)%>
                        </td>
                    </tr>

                    <tr>
                        <th><%: Html.LabelFor(model => model.DivHead)%></th>
                        <td>
                            <%: Html.DropDownListFor(model => model.DivHead, ViewBag.employees as SelectList, "")%>
                            <%: Html.ValidationMessageFor(model => model.DivHead)%>
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


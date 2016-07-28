/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2016 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpcya.com                                 *
 *****************************************************************************/
package org.compiere.process;

import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MPaySelection;
import org.compiere.model.MPaySelectionLine;

/**
 * 	Payment Selection Create From Invoice, used for Smart Browse (Create From Invoice)
 *	@author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
 *		<li> FR [ 297 ] Payment Selection must be like ADempiere Document
 *		@see https://github.com/adempiere/adempiere/issues/297
 */
public class PSCreateFromInvoice extends SvrProcess {

	/**	Sequence			*/
	private int				m_SeqNo = 10;
	
	@Override
	protected void prepare() {
		//	Valid Record Identifier
		if(getRecord_ID() <= 0)
			throw new AdempiereException("@C_PaySelection_ID@ @NotFound@");
	}

	@Override
	protected String doIt() throws Exception {
		//	Instance current Payment Selection
		MPaySelection paySelection = new MPaySelection(getCtx(), getRecord_ID(), get_TrxName());
		m_SeqNo = paySelection.getLastLineNo();
		//	Loop for keys
		for(Integer key : getSelectionKeys()) {
			//	get values from result set
			int C_Invoice_ID = key;
			int C_InvoicePaySchedule_ID = getSelectionAsInt(key, "INV_C_InvoicePaySchedule_ID");
			String PaymentRule = getSelectionAsString(key, "INV_PaymentRule");
			BigDecimal AmtSource = getSelectionAsBigDecimal(key, "INV_AmtSource");
			BigDecimal OpenAmt = getSelectionAsBigDecimal(key, "INV_OpenAmt");
			BigDecimal PayAmt = getSelectionAsBigDecimal(key, "INV_PayAmt");
			BigDecimal DiscountAmt = getSelectionAsBigDecimal(key, "INV_DiscountAmt");
			m_SeqNo += 10;
			MPaySelectionLine line = new MPaySelectionLine(paySelection, m_SeqNo, PaymentRule);
			//	Add Order
			line.setInvoice(C_Invoice_ID, C_InvoicePaySchedule_ID, AmtSource, OpenAmt, PayAmt, DiscountAmt);
			//	Save
			line.saveEx();
		}
		//	Default Ok
		return "@OK@";
	}
}

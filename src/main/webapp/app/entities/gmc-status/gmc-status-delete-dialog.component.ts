import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {GmcStatus} from "./gmc-status.model";
import {GmcStatusPopupService} from "./gmc-status-popup.service";
import {GmcStatusService} from "./gmc-status.service";

@Component({
	selector: 'jhi-gmc-status-delete-dialog',
	templateUrl: './gmc-status-delete-dialog.component.html'
})
export class GmcStatusDeleteDialogComponent {

	gmcStatus: GmcStatus;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gmcStatusService: GmcStatusService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['gmcStatus']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.gmcStatusService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'gmcStatusListModification',
				content: 'Deleted an gmcStatus'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-gmc-status-delete-popup',
	template: ''
})
export class GmcStatusDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gmcStatusPopupService: GmcStatusPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.gmcStatusPopupService
				.open(GmcStatusDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}

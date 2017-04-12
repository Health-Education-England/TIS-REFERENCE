import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {GenderDetailComponent} from "../../../../../../main/webapp/app/entities/gender/gender-detail.component";
import {GenderService} from "../../../../../../main/webapp/app/entities/gender/gender.service";
import {Gender} from "../../../../../../main/webapp/app/entities/gender/gender.model";

describe('Component Tests', () => {

	describe('Gender Management Detail Component', () => {
		let comp: GenderDetailComponent;
		let fixture: ComponentFixture<GenderDetailComponent>;
		let service: GenderService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GenderDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					GenderService
				]
			}).overrideComponent(GenderDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GenderDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GenderService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Gender(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.gender).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
